package cc.zerite.client.util.rendering

object EasingUtil {

    fun easeIn(t: Float, b: Float, c: Float, d: Float): Float {
        return c * (t / d) * t * t * t * t + b
    }

    fun easeOut(t: Float, b: Float, c: Float, d: Float): Float {
        return c * ((t / d - 1) * t * t * t * t + 1) + b
    }

    fun easeInOut(t: Float, b: Float, c: Float, d: Float): Float {
        return if ((t / d / 2) < 1) {
            c / 2 * t * t * t * t * t + b
        } else c / 2 * ((t - 2f) * t * t * t * t + 2) + b
    }

}