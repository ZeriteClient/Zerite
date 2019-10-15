package cc.zerite.client.game.tools.font

import cc.zerite.client.util.rendering.ColorUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.EnumChatFormatting
import org.lwjgl.opengl.GL11
import org.newdawn.slick.UnicodeFont
import org.newdawn.slick.font.effects.ColorEffect
import java.awt.Color
import java.awt.Font
import java.util.*


class ZeriteFontRenderer(private val fontImpl: Font, val size: Float, private var antiAliasingFactor: Int) {

    //    private var font: UnicodeFont = UnicodeFont(fontImpl.deriveFont(size * antiAliasingFactor))
    private var prevScaleFactor: Int = ScaledResolution(Minecraft.getMinecraft()).scaleFactor
    private var font: UnicodeFont = UnicodeFont(fontImpl.deriveFont(size * prevScaleFactor / 2))
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

    fun drawString(text: String, x: Int, y: Int, color: Int, shadow: Boolean, chroma: Boolean = false) {
        val textureEnabled = GL11.glIsEnabled(GL11.GL_TEXTURE_2D)
        val resolution = ScaledResolution(Minecraft.getMinecraft())

        if (resolution.scaleFactor != prevScaleFactor) {
            prevScaleFactor = resolution.scaleFactor
            font = UnicodeFont(fontImpl.deriveFont(size * prevScaleFactor / 2))
            font.addAsciiGlyphs()
            font.effects.add(ColorEffect(Color.WHITE))
            font.loadGlyphs()
        }

        antiAliasingFactor = resolution.scaleFactor

        val posX: Float = (x * antiAliasingFactor).toFloat()
        val posY: Float = (y * antiAliasingFactor).toFloat()

        val chars = text.toCharArray()

        val red = (color shr 16 and 255).toFloat() / 255.0f
        val green = (color shr 8 and 255).toFloat() / 255.0f
        val blue = (color and 255).toFloat() / 255.0f
        val alpha = (color shr 24 and 255).toFloat() / 255.0f

        GL11.glColor4f(red, green, blue, alpha)

//        GlStateManager.scale(1f / , 1f / resolution.scaleFactor, 1f / resolution.scaleFactor)
        GlStateManager
            .scale(1f / antiAliasingFactor, 1f / antiAliasingFactor, 1f / antiAliasingFactor)

        // TODO: Fix this
//        GLUtil.begin {
//            changeState(GL11.GL_TEXTURE_2D, false)
//            changeState(GL11.GL_LIGHTING, false)
//            changeState(GL11.GL_BLEND, true)
//        }

        GlStateManager.disableTexture2D()
        GlStateManager.disableLighting()
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

        var currentX: Float = posX
        var currentY: Float = posY
        var currentColor = color

        for (i in chars.indices) {
            val c = chars[i]

            if (c == '\n') {
                currentY += getHeight(text) / 4
                currentX = posX
            } else if (c != '\u00a7' && (i == 0 || i == chars.size - 1 || chars[i - 1] != '\u00a7')) {
                if ((i > 0 && chars[i - 1] == '\u00a7' && i == chars.size - 1) || alpha == 0.0f) {
                    continue
                }

                val charStr = c.toString()

                font.drawString(
                    currentX,
                    currentY,
                    charStr,
                    org.newdawn.slick.Color(if (chroma) ColorUtil.rainbowColor((currentX - currentY) * 6).rgb else currentColor)
                )

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
//        GlStateManager.scale(resolution.scaleFactor.toDouble(), resolution.scaleFactor.toDouble(), resolution.scaleFactor.toDouble())

        GL11.glColor4f(1f, 1f, 1f, 1f)

        GlStateManager.bindTexture(0)

        if (textureEnabled)
            GlStateManager.enableTexture2D()

//        GLUtil.end()
    }

    fun drawString(text: String, x: Int, y: Int, color: Int) {
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
        return font.getHeight(text) / antiAliasingFactor
    }

    fun getWidth(text: String): Int {
        return font.getWidth(EnumChatFormatting.getTextWithoutFormattingCodes(text)) / antiAliasingFactor
    }
}