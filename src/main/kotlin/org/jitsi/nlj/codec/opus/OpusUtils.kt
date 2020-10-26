/*
 * Copyright @ 2020 - present 8x8, Inc.
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

package org.jitsi.nlj.codec.opus

class OpusUtils {
    companion object {
        /**
         * Get the duration of an Opus packet, in clock samples (48 kHz clock).
         */
        fun getOpusDuration(data: ByteArray, offset: Int, len: Int): Int {
            check(data.size >= offset + len)
            check(len > 0)

            val tocByte = data[offset].toInt() and 0xff

            val configVal = tocByte shr 3
            val packetCode = tocByte and 3

            val frameDurationSamples = when (configVal) {
                /* CELT NB, WB, SWB, FB 2.5 ms */
                16, 20, 24, 28 -> 120
                /* CELT NB, WB, SWB, FB 5 ms */
                17, 21, 25, 29 -> 240
                /* SILK NB, MB, WB; Hybrid SWB, FB; CELT NB, WB, SWB, FB 10 ms */
                0, 4, 8, 12, 14, 18, 22, 26, 30 -> 480
                /* SILK NB, MB, WB; Hybrid SWB, FB; CELT NB, WB, SWB, FB 20 ms */
                1, 5, 9, 13, 15, 19, 23, 27, 31 -> 960
                /* SILK NB, MB, WB 40 ms */
                2, 6, 10 -> 1920
                /* SILK NB, MB, WB 60 ms */
                3, 7, 11 -> 2880
                else ->
                    throw IllegalStateException("Impossible math")
            }

            val numFrames = when (packetCode) {
                0 -> 1
                1, 2 -> 2
                3 -> {
                    check(len > 1)
                    // TODO: This will give a bad value for code 3 packets in our current implementation of E2EE,
                    //  which sends only the first Opus byte in the clear.  Should we validate the count vs. the
                    //  ostensible in-packet frame length bytes? (Note that code 3 packets should be very rare for
                    //  normal ptime values from browsers.)
                    data[offset + 1].toInt() and 0x3f
                }
                else ->
                    throw IllegalStateException("Impossible math")
            }

            return frameDurationSamples * numFrames
        }
    }
}
