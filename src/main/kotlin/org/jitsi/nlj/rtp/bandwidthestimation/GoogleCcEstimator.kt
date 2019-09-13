package org.jitsi.nlj.rtp.bandwidthestimation

import java.time.Duration
import java.time.Instant
import kotlin.properties.Delegates
import org.jitsi.nlj.stats.NodeStatsBlock
import org.jitsi.nlj.util.createChildLogger
import org.jitsi.utils.logging.DiagnosticContext
import org.jitsi.utils.logging2.Logger
import org.jitsi_modified.impl.neomedia.rtp.remotebitrateestimator.RemoteBitrateEstimatorAbsSendTime
import org.jitsi_modified.impl.neomedia.rtp.sendsidebandwidthestimation.SendSideBandwidthEstimation

private const val defaultInitBw: Float = 2_500_000.0f
private const val defaultMinBw: Float = 30_000.0f
private const val defaultMaxBw: Float = 20_000_000.0f

class GoogleCcEstimator(diagnosticContext: DiagnosticContext, parentLogger: Logger) : BandwidthEstimator {
    override val algorithmName = "Google CC"

    /* TODO: Use configuration service to set this default value. */
    override var initBw: Float = defaultInitBw
    /* TODO: observable which sets the components' values if we're in initial state. */

    override var minBw: Float by Delegates.observable(defaultMinBw) {
        property, oldValue, newValue ->
        bitrateEstimatorAbsSendTime.setMinBitrate(newValue.toInt())
        sendSideBandwidthEstimation.setMinMaxBitrate(newValue.toInt(), maxBw.toInt())
    }

    override var maxBw: Float by Delegates.observable(defaultMaxBw) {
        property, oldValue, newValue ->
        sendSideBandwidthEstimation.setMinMaxBitrate(minBw.toInt(), newValue.toInt())
    }

    private val logger = parentLogger.createChildLogger(GoogleCcEstimator::class)

    /**
     * Implements the delay-based part of Google CC.
     */
    private val bitrateEstimatorAbsSendTime = RemoteBitrateEstimatorAbsSendTime(null, diagnosticContext, logger)
    init {
        bitrateEstimatorAbsSendTime.setMinBitrate(minBw.toInt())
    }

    /**
     * Implements the loss-based part of Google CC.
     */
    private val sendSideBandwidthEstimation = SendSideBandwidthEstimation(diagnosticContext, initBw.toLong(), logger)
    init {
        sendSideBandwidthEstimation.setMinMaxBitrate(minBw.toInt(), maxBw.toInt())
    }

    override fun processPacketArrival(now: Instant, sendTime: Instant?, recvTime: Instant?, seq: Int, size: Int, ecn: Byte) {
        TODO("not implemented")
    }

    override fun processPacketLoss(now: Instant, sendTime: Instant?, seq: Int) {
        TODO("not implemented")
    }

    override fun onRttUpdate(now: Instant, newRtt: Duration) {
        bitrateEstimatorAbsSendTime.onRttUpdate(now.toEpochMilli(), newRtt.toMillis(), -1)
        sendSideBandwidthEstimation.onRttUpdate(newRtt.toNanos() / 1.0e9)
    }

    override fun getCurrentBw(now: Instant): Float {
        return sendSideBandwidthEstimation.latestEstimate.toFloat()
    }

    override fun getStats(): NodeStatsBlock {
        TODO("not implemented")
    }

    override fun reset() {
        initBw = defaultInitBw
        minBw = defaultMinBw
        maxBw = defaultMaxBw

        bitrateEstimatorAbsSendTime.reset()
        sendSideBandwidthEstimation.reset(initBw.toLong())

        sendSideBandwidthEstimation.setMinMaxBitrate(minBw.toInt(), maxBw.toInt())
    }
}
