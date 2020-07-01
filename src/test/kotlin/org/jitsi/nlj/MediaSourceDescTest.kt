/*
 * Copyright @ 2018 - present 8x8, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jitsi.nlj

import io.kotlintest.IsolationMode
import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec
import org.jitsi.utils.stats.RateStatistics

class MediaSourceDescTest : ShouldSpec() {
    override fun isolationMode(): IsolationMode? = IsolationMode.InstancePerLeaf

    init {
        val ssrcs = arrayOf(0xdeadbeef, 0xcafebabe, 0x01234567)
        val source = createSource(ssrcs,
            1, 3, "Fake owner")

        source.owner shouldBe "Fake owner"
        source.rtpEncodings.size shouldBe 3

        source.rtpLayers.size shouldBe 9
        source.hasRtpLayers() shouldBe true
        source.numRtpLayers() shouldBe 9

        source.matches(0xdeadbeef) shouldBe true

        for (i in source.rtpEncodings.indices) {
            val e = source.rtpEncodings[i]
            e.primarySSRC shouldBe ssrcs[i]
            e.layers.size shouldBe 3
            for (j in e.layers.indices) {
                val l = e.layers[j]
                l.eid shouldBe i
                l.tid shouldBe j
                l.sid shouldBe -1

                /* Set up rate statistics testing */
                /* Set non-zero rates for (0,0), (0,1), (1,0), (1,1), and (2,2) */
                if (!(i != source.rtpEncodings.size - 1) xor (j != e.layers.size - 1)) {
                    /* Use a bitmask so it's unambiguous which layers are getting summed. */
                    l.inheritStatistics(FakeRateStatistics(1L shl (i * source.rtpEncodings.size + j)))
                }
            }
        }

        val t = System.currentTimeMillis() // Doesn't actually matter for fake rate statistics

        /* Rate statistics accumulate across dependencies */
        source.getBitrateBps(t, RtpLayerDesc.getIndex(0, 0, 0)) shouldBe 0x1
        source.getBitrateBps(t, RtpLayerDesc.getIndex(0, 0, 1)) shouldBe 0x3
        source.getBitrateBps(t, RtpLayerDesc.getIndex(0, 0, 2)) shouldBe 0x3

        source.getBitrateBps(t, RtpLayerDesc.getIndex(1, 0, 0)) shouldBe 0x8
        source.getBitrateBps(t, RtpLayerDesc.getIndex(1, 0, 1)) shouldBe 0x18
        source.getBitrateBps(t, RtpLayerDesc.getIndex(1, 0, 2)) shouldBe 0x18

        /* If a layer returns a 0 rate, the function gets the next lower non-zero rate */
        source.getBitrateBps(t, RtpLayerDesc.getIndex(2, 0, 0)) shouldBe 0x18
        source.getBitrateBps(t, RtpLayerDesc.getIndex(2, 0, 1)) shouldBe 0x18

        source.getBitrateBps(t, RtpLayerDesc.getIndex(2, 0, 2)) shouldBe 0x100
    }
}

/* The following creation functions are adapted from MediaSourceFactory in jitsi-videobridge. */

/**
 * Calculates the array position of an RTP layer description specified by its
 * spatial index (SVC) and temporal index (SVC).
 *
 * @param spatialIdx the spatial layer index.
 * @param temporalIdx the temporal layer index.
 *
 * @return the subjective quality index of the flow specified in the
 * arguments.
 */
private fun idx(spatialIdx: Int, temporalIdx: Int, temporalLen: Int) =
    spatialIdx * temporalLen + temporalIdx

/*
 * Creates layers for an encoding.
 *
 * @param spatialLen the number of spatial encodings per simulcast stream.
 * @param temporalLen the number of temporal encodings per simulcast stream.
 * @param height the maximum height of the top spatial layer
 * @return an array that holds the layer descriptions.
 */
private fun createRTPLayerDescs(
    spatialLen: Int,
    temporalLen: Int,
    encodingIdx: Int,
    height: Int
): Array<RtpLayerDesc> {
    val rtpLayers = arrayOfNulls<RtpLayerDesc>(spatialLen * temporalLen)
    for (spatialIdx in 0 until spatialLen) {
        var frameRate = 30.toDouble() / (1 shl temporalLen - 1)
        for (temporalIdx in 0 until temporalLen) {
            val idx: Int = idx(spatialIdx, temporalIdx, temporalLen)
            var dependencies: Array<RtpLayerDesc>?
            dependencies = if (spatialIdx > 0 && temporalIdx > 0) {
                // this layer depends on spatialIdx-1 and temporalIdx-1.
                arrayOf(
                    rtpLayers[idx(spatialIdx, temporalIdx - 1,
                        temporalLen)]!!,
                    rtpLayers[idx(spatialIdx - 1, temporalIdx,
                        temporalLen)]!!
                )
            } else if (spatialIdx > 0) {
                // this layer depends on spatialIdx-1.
                arrayOf(rtpLayers[idx(spatialIdx - 1, temporalIdx,
                    temporalLen)]!!)
            } else if (temporalIdx > 0) {
                // this layer depends on temporalIdx-1.
                arrayOf(rtpLayers[idx(spatialIdx, temporalIdx - 1,
                    temporalLen)]!!)
            } else {
                // this is a base layer without any dependencies.
                null
            }
            val temporalId = if (temporalLen > 1) temporalIdx else -1
            val spatialId = if (spatialLen > 1) spatialIdx else -1
            rtpLayers[idx] = RtpLayerDesc(encodingIdx,
                temporalId, spatialId, height, frameRate, dependencies)
            frameRate *= 2.0
        }
    }
    return rtpLayers as Array<RtpLayerDesc>
}

/**
 * Creates an RTP encoding.
 * @param primarySsrc the primary SSRC for the encoding.
 * @param spatialLen the number of spatial layers of the encoding.
 * @param temporalLen the number of temporal layers of the encodings.
 * @param secondarySsrcs a list of pairs, where each
 * pair has the secondary ssrc as its key, and the type (rtx, etc.) as its
 * value
 * @param encodingIdx the index of the encoding
 * @return a description of the encoding.
 */
private fun createRtpEncodingDesc(
    primarySsrc: Long,
    spatialLen: Int,
    temporalLen: Int,
    encodingIdx: Int,
    height: Int
): RtpEncodingDesc {
    val layers: Array<RtpLayerDesc> = createRTPLayerDescs(spatialLen, temporalLen,
        encodingIdx, height)
    val enc = RtpEncodingDesc(primarySsrc, layers)
    return enc
}

private fun createSource(
    primarySsrcs: Array<Long>,
    numSpatialLayersPerStream: Int,
    numTemporalLayersPerStream: Int,
    owner: String
): MediaSourceDesc {
    var height = 720

    val encodings = Array(primarySsrcs.size) {
        encodingIdx ->
        val primarySsrc: Long = primarySsrcs.get(encodingIdx)
        val ret = createRtpEncodingDesc(primarySsrc,
            numSpatialLayersPerStream, numTemporalLayersPerStream, encodingIdx, height)
        height *= 2
        ret
    }

    return MediaSourceDesc(encodings, owner)
}

/** A fake rate statistics object, for testing */
private class FakeRateStatistics(
    private val fakeRate: Long
) : RateStatistics(RtpLayerDesc.AVERAGE_BITRATE_WINDOW_MS) {
    override fun getRate(nowMs: Long): Long {
        return fakeRate
    }
}
