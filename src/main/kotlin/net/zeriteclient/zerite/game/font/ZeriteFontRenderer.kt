package net.zeriteclient.zerite.game.font

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.EnumChatFormatting
import org.lwjgl.opengl.GL11
import org.newdawn.slick.UnicodeFont
import org.newdawn.slick.font.effects.ColorEffect
import java.awt.Color
import java.awt.Font
import java.util.*


class ZeriteFontRenderer(fontImpl: Font, size: Float, private val antiAliasingFactor: Int) {

    private var font: UnicodeFont = UnicodeFont(fontImpl.deriveFont(size * antiAliasingFactor))
    private var colorCodes: IntArray

    init {
        font.addAsciiGlyphs()
        font.effects.add(ColorEffect(Color.WHITE))
        font.loadGlyphs()
        this.colorCodes = IntArray(32)
        for (i in 0..31) {
            val shadow = (i shr 3 and 1) * 85
            var red = (i shr 2 and 1) * 170 + shadow
            var green = (i shr 1 and 1) * 170 + shadow
            var blue = (i and 1) * 170 + shadow

            if (i >= 16) {
                red /= 4
                green /= 4
                blue /= 4
            }

            this.colorCodes[i] = red and 255 shl 16 or (green and 255 shl 8) or (blue and 255)
        }
    }

    private fun drawString(text: String, x: Int, y: Int, color: Int, shadow: Boolean) {
        val posX: Float = (x * antiAliasingFactor).toFloat()
        val posY: Float = (y * antiAliasingFactor).toFloat()

        val chars = text.toCharArray()

        val red = (color shr 16 and 255).toFloat() / 255.0f
        val green = (color shr 8 and 255).toFloat() / 255.0f
        val blue = (color and 255).toFloat() / 255.0f
        val alpha = (color shr 24 and 255).toFloat() / 255.0f

        GL11.glColor4f(red, green, blue, alpha)

        GlStateManager
            .scale(1f / antiAliasingFactor, 1f / antiAliasingFactor, 1f / antiAliasingFactor)

        GlStateManager.disableTexture2D()
        GlStateManager.disableLighting()
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.blendFunc(770, 771)

        var currentX: Float = posX
        var currentColor = color

        for (i in 0 until chars.size) {
            val c = chars[i]

            if (c != '\u00a7' && (i == 0 || i == chars.size - 1 || chars[i - 1] != '\u00a7')) {
                if (i > 0 && chars[i - 1] == '\u00a7' && i == chars.size - 1) {
                    continue
                }

                val charStr = Character.toString(c)

                font.drawString(currentX, posY, charStr, org.newdawn.slick.Color(currentColor))

                currentX += font.getWidth(charStr)
            } else if (c == '\u00a7' && i != chars.size - 1) {
                var index = "0123456789abcdefklmnor"
                    .indexOf(text.toLowerCase(Locale.ENGLISH)[i + 1])

                if (index < 0) {
                    continue
                }

                if (shadow) {
                    index += 16
                }

                val colorResult = colorCodes[index]

                val rRed = colorResult shr 16 and 255
                val rGreen = colorResult shr 8 and 255
                val rBlue = colorResult and 255
                val rAlpha = colorResult shr 24 and 255

                currentColor = java.awt.Color(rRed, rGreen, rBlue, if (shadow) 150 else rAlpha)
                    .rgb
            }
        }

        GlStateManager.scale(
            antiAliasingFactor.toDouble(),
            antiAliasingFactor.toDouble(),
            antiAliasingFactor.toDouble()
        )
        GL11.glColor4f(1f, 1f, 1f, 1f)

        GlStateManager.disableTexture2D()
    }

    private fun drawString(text: String, x: Int, y: Int, color: Int) {
        drawString(text, x, y, color, false)
    }

    private fun drawStringWithShadow(text: String, x: Int, y: Int, color: Int) {
        drawString(text, x + 1, y + 1, Color(0, 0, 0, 150).rgb, true)
        drawString(text, x, y, color)
    }

    fun drawCenteredString(text: String, x: Int, y: Int, color: Int) {
        drawString(text, x - getWidth(text) / 2, y, color)
    }

    private fun drawCenteredStringWithShadow(text: String, x: Int, y: Int, color: Int) {
        drawStringWithShadow(text, x - getWidth(text) / 2, y, color)
    }

    fun getHeight(text: String): Int {
        return font.getHeight(text) / 4 * antiAliasingFactor
    }

    private fun getWidth(text: String): Int {
        return font.getWidth(EnumChatFormatting.getTextWithoutFormattingCodes(text)) / antiAliasingFactor
    }
}