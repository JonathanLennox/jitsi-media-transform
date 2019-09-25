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

package org.jitsi.nlj.util

import java.text.DecimalFormat

/**
 * Model an amount of data, internally represented as a number of bits.
 */
class DataSize(
    bits: Long
) : Comparable<DataSize> {

    var bits: Long = bits
        private set

    val bytes: Long = bits / 8
    val kiloBytes: Long = bytes / 1000
    val megaBytes: Long = kiloBytes / 1000

    operator fun minus(other: DataSize): DataSize =
        DataSize(bits - other.bits)

    operator fun minusAssign(other: DataSize) {
        bits -= other.bits
    }

    operator fun plus(other: DataSize): DataSize =
        DataSize(bits + other.bits)

    operator fun plusAssign(other: DataSize) {
        bits += other.bits
    }

    operator fun times(other: Int): DataSize =
        DataSize(bits * other)

    operator fun timesAssign(other: Int) {
        bits *= other
    }

    override fun toString(): String {
        // To determine which unit we'll print in,
        // find the biggest one which has a value
        // in the ones place
        val format = DecimalFormat("0.##")
        return when {
            megaBytes >= 1 -> "${format.format(megaBytes)} MB"
            kiloBytes >= 1 -> "${format.format(kiloBytes)} KB"
            bytes >= 1 -> "${format.format(bytes)} B"
            else -> "${format.format(bits)} bits"
        }
    }

    override fun compareTo(other: DataSize): Int = when {
        bits < other.bits -> -1
        bits > other.bits -> 1
        else -> 0
    }

    override fun equals(other: Any?): Boolean {
        if (other !is DataSize) {
            return false
        }
        return compareTo(other) == 0
    }

    override fun hashCode(): Int = bits.hashCode()
}

fun Int.bits(): DataSize = DataSize(this.toLong())
fun Int.bytes(): DataSize = DataSize(this.toLong() * 8)
fun Int.kilobytes(): DataSize = DataSize(this.toLong() * 1000 * 8)
fun Int.megabytes(): DataSize = DataSize(this.toLong() * 1000 * 1000 * 8)

fun Long.bits(): DataSize = DataSize(this)
fun Long.bytes(): DataSize = DataSize(this * 8)
fun Long.kilobytes(): DataSize = DataSize(this * 1000 * 8)
fun Long.megabytes(): DataSize = DataSize(this * 1000 * 1000 * 8)
