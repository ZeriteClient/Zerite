package net.zeriteclient.zerite.util.rendering


object AnimationUtil {

    private fun easeIn(t: Double, power: Double) = Math.pow(t, power)
    private fun easeOut(t: Double, power: Double) = 1 - Math.abs(Math.pow(t - 1, power))
    private fun easeInOut(t: Double, power: Double) =
        if (t < .5) easeIn(t * 2, power) / 2 else easeOut(t * 2 - 1, power) / 2 + 0.5

    fun easeIn(start: Double, end: Double, startTime: Long, duration: Double, power: Double = 3.0) = start + easeIn(Math.min(1.0, (System.currentTimeMillis() - startTime) / duration), power) * end
    fun easeOut(start: Double, end: Double, startTime: Long, duration: Double, power: Double = 3.0) = start + easeOut(Math.min(1.0, (System.currentTimeMillis() - startTime) / duration), power) * end
    fun easeInOut(start: Double, end: Double, startTime: Long, duration: Double, power: Double = 3.0) = start + easeInOut(Math.min(1.0, (System.currentTimeMillis() - startTime) / duration), power) * end

}