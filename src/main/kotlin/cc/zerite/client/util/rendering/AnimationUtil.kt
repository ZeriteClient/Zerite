package cc.zerite.client.util.rendering

import kotlin.math.abs
import kotlin.math.min
import kotlin.math.pow


object AnimationUtil {

    private fun easeIn(t: Double, power: Double) = t.pow(power)
    private fun easeOut(t: Double, power: Double) = 1 - abs((t - 1).pow(power))
    private fun easeInOut(t: Double, power: Double) =
        if (t < .5) easeIn(t * 2, power) / 2 else easeOut(t * 2 - 1, power) / 2 + 0.5

    fun easeIn(start: Double, end: Double, startTime: Long, duration: Double, power: Double = 3.0) =
        start + easeIn(min(1.0, (System.currentTimeMillis() - startTime) / duration), power) * end

    fun easeOut(start: Double, end: Double, startTime: Long, duration: Double, power: Double = 3.0) = start + easeOut(
        min(1.0, (System.currentTimeMillis() - startTime) / duration), power
    ) * end

    fun easeInOut(start: Double, end: Double, startTime: Long, duration: Double, power: Double = 3.0) =
        start + easeInOut(
            min(1.0, (System.currentTimeMillis() - startTime) / duration), power
        ) * end

}