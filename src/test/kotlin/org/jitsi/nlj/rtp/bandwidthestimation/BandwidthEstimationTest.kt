package org.jitsi.nlj.rtp.bandwidthestimation

import com.nhaarman.mockitokotlin2.spy
import io.kotlintest.matchers.doubles.shouldBeGreaterThan
import io.kotlintest.matchers.doubles.shouldBeLessThan
import io.kotlintest.specs.ShouldSpec
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.ArrayDeque
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates
import org.jitsi.nlj.test_utils.FakeScheduledExecutorService
import org.jitsi.nlj.util.Bandwidth
import org.jitsi.nlj.util.NEVER
import org.jitsi.nlj.util.bps
import org.jitsi.nlj.util.mbps
import org.jitsi.service.libjitsi.LibJitsi
import org.jitsi.utils.logging.DiagnosticContext
import org.jitsi.utils.logging.TimeSeriesLogger
import org.jitsi.utils.logging2.LoggerImpl

/** A simulated packet, for bandwidth estimation testing. */
data class SimulatedPacket(
    val sendTime: Instant,
    val packetSize: Int,
    val ssrc: Long
)

/* TODO: move this to some util class */
const val NANOSECONDS_PER_SECOND = 1.0e9f

abstract class FixedRateSender(
    val executor: ScheduledExecutorService,
    val clock: Clock,
    var receiver: (SimulatedPacket) -> Unit
) {
    var nextPacket: ScheduledFuture<*>? = null
    var lastSendTime: Instant = NEVER

    var running = false

    var rate: Bandwidth by Delegates.observable(0.bps) {
        _, _, newValue ->
        nextPacket?.cancel(false)
        schedulePacket(false)
    }

    abstract fun nextPacketSize(): Int

    fun schedulePacket(justSent: Boolean) {
        if (!running || rate <= 0.bps || nextPacketSize() == 0) {
            nextPacket = null
        } else {
            val packetDelayTime = when (lastSendTime) {
                NEVER -> 0
                else -> {
                    var delayTime = (nextPacketSize() * Byte.SIZE_BITS * NANOSECONDS_PER_SECOND / rate.bps).toLong()
                    if (!justSent) {
                        delayTime -= Duration.between(lastSendTime, clock.instant()).toNanos()
                    }
                    delayTime
                }
            }

            nextPacket = executor.schedule(::doSendPacket, packetDelayTime, TimeUnit.NANOSECONDS)
        }
    }

    fun doSendPacket() {
        val now = clock.instant()
        val sendNext = sendPacket(now)

        if (sendNext) {
            lastSendTime = now
            schedulePacket(true)
        }
    }

    abstract fun sendPacket(now: Instant): Boolean

    fun start() {
        running = true
        doSendPacket()
    }

    fun stop() {
        running = false
        nextPacket?.cancel(false)
    }
}

class PacketGenerator(
    executor: ScheduledExecutorService,
    clock: Clock,
    receiver: (SimulatedPacket) -> Unit,
    var packetSize: Int = 1250,
    val ssrc: Long = 0xcafebabe
) : FixedRateSender(executor, clock, receiver) {
    override fun nextPacketSize() = packetSize

    override fun sendPacket(now: Instant): Boolean {
        val packet = SimulatedPacket(now, packetSize, ssrc)
        receiver(packet)
        return true
    }
}

class PacketBottleneck(
    executor: ScheduledExecutorService,
    clock: Clock,
    val ctx: DiagnosticContext,
    receiver: (SimulatedPacket) -> Unit
) : FixedRateSender(executor, clock, receiver) {
    val timeSeriesLogger = TimeSeriesLogger.getTimeSeriesLogger(this.javaClass)

    val queue = ArrayDeque<SimulatedPacket>()

    fun enqueue(packet: SimulatedPacket) {
        if (!running) {
            return
        }
        assert(packet.sendTime <= clock.instant())
        val queueWasEmpty = queue.isEmpty()
        queue.addFirst(packet)
        if (queueWasEmpty) {
            schedulePacket(false)
        }
    }

    override fun sendPacket(now: Instant): Boolean {
        if (queue.isEmpty()) {
            return false
        }

        val packet = queue.removeLast()
        receiver(packet)

        if (timeSeriesLogger.isTraceEnabled) {
            val delay = Duration.between(packet.sendTime, now)
            timeSeriesLogger.trace(ctx.makeTimeSeriesPoint("queue", now)
                .addField("depth", queue.size)
                .addField("delay", delay.toNanos() / 1e6)
            )
        }

        return true
    }

    override fun nextPacketSize(): Int = queue.peek()?.packetSize ?: 0
}

class PacketDelayer(
    val executor: ScheduledExecutorService,
    val receiver: (SimulatedPacket) -> Unit,
    val delay: Duration
) {
    fun enqueue(packet: SimulatedPacket) {
        executor.schedule({ receiver(packet) }, delay.toNanos(), TimeUnit.NANOSECONDS)
    }
}

class PacketReceiver(
    val clock: Clock,
    val estimator: BandwidthEstimator,
    val ctx: DiagnosticContext,
    val rateReceiver: (Bandwidth) -> Unit
) {
    val timeSeriesLogger = TimeSeriesLogger.getTimeSeriesLogger(this.javaClass)
    var seq = 0

    fun receivePacket(packet: SimulatedPacket) {
        val now = clock.instant()
        assert(packet.sendTime <= now)
        /* All delay is send -> receive in this simulation, so one-way delay is rtt. */
        estimator.onRttUpdate(now, Duration.between(packet.sendTime, now))
        estimator.processPacketArrival(now, packet.sendTime, now, seq, packet.packetSize)
        seq++
        val bw = estimator.getCurrentBw(now)
        if (timeSeriesLogger.isTraceEnabled) {
            timeSeriesLogger.trace(ctx.makeTimeSeriesPoint("bw", now).addField("bw", bw))
        }
        rateReceiver(bw)
    }
}

class BandwidthEstimationTest : ShouldSpec() {
    init {
        /* Internals of GoogleCc use ConfigurationService at construct time. */
        LibJitsi.start()
    }
    private val scheduler: FakeScheduledExecutorService = spy()
    val clock: Clock = scheduler.clock

    val ctx = DiagnosticContext(clock)
    init {
        /* Emulate the fields that jitsi-videobridge puts in its DiagnosticContexts. */
        ctx["conf_name"] = "BandwidthEstimationTest"
        ctx["conf_creation_time_ms"] = clock.instant().toEpochMilli()
        ctx["endpoint_id"] = "00000000"
    }

    val logger = LoggerImpl(BandwidthEstimationTest::class.qualifiedName)
    val estimator: BandwidthEstimator = GoogleCcEstimator(ctx, logger)

    val rtt = Duration.ofMillis(200)
    val bottleneckRate = 4.mbps

    val generator: PacketGenerator = PacketGenerator(scheduler, clock, { bottleneck.enqueue(it) })
    val bottleneck: PacketBottleneck = PacketBottleneck(scheduler, clock, ctx, { delayer.enqueue(it) })
    val delayer: PacketDelayer = PacketDelayer(scheduler, { receiver.receivePacket(it) }, rtt)
    val receiver: PacketReceiver = PacketReceiver(clock, estimator, ctx, { generator.rate = it })

    init {
        "Running bandwidth estimation test" {
            should("work correctly") {
                bottleneck.rate = bottleneckRate
                generator.rate = estimator.getCurrentBw(clock.instant())

                bottleneck.start()
                generator.start()

                scheduler.runUntil(clock.instant().plus(120, ChronoUnit.SECONDS))

                generator.stop()
                bottleneck.stop()

                val finalBw = estimator.getCurrentBw(clock.instant())
                finalBw.bps.shouldBeGreaterThan((bottleneckRate / 1.2).bps)
                finalBw.bps.shouldBeLessThan((bottleneckRate * 1.2).bps)
            }
        }
    }
}
