package net.zeriteclient.zerite.util.rendering

import java.awt.Color

object ColorUtil {

    fun rainbowColor(offset: Float): Color {
        var rainbowState: Double = Math.ceil((System.currentTimeMillis().toDouble() + offset) / 10)
        rainbowState %= 360.0f
        println(rainbowState / 360.0f)
        return Color.getHSBColor((rainbowState / 360.0f).toFloat(), 0.8f, 0.8f)
    }

}