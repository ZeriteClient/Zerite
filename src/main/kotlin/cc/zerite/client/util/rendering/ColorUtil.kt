package cc.zerite.client.util.rendering

import java.awt.Color
import kotlin.math.ceil

object ColorUtil {

    fun rainbowColor(offset: Float): Color {
        var rainbowState: Double = ceil((System.currentTimeMillis().toDouble() + offset) / 10)
        rainbowState %= 360.0f
        return Color.getHSBColor((rainbowState / 360.0f).toFloat(), 0.8f, 0.8f)
    }

}