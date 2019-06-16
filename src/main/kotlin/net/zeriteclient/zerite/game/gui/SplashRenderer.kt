package net.zeriteclient.zerite.game.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.client.shader.Framebuffer
import net.zeriteclient.zerite.game.tools.font.ZeriteFonts
import net.zeriteclient.zerite.util.EnumBackground
import net.zeriteclient.zerite.util.rendering.ShapeUtil
import java.awt.Color

object SplashRenderer {

    private var progress = 1
    private var text = "Initializing"

    /**
     * Draws the custom splash
     *
     * @param tm [TextureManager] The texture manager to use
     * @param overlay [Boolean] Whether this is in overlay mode
     * @param opacity [Integer] The opacity out of 255
     */
    fun drawSplash(tm: TextureManager, overlay: Boolean, opacity: Int) {
        // Get resolution
        val scaledResolution = ScaledResolution(Minecraft.getMinecraft())
        val i = scaledResolution.scaleFactor

        // Create framebuffer
        var framebuffer: Framebuffer? = null
        if (!overlay) {
            // Assign framebuffer
            framebuffer = Framebuffer(
                scaledResolution.scaledWidth * i,
                scaledResolution.scaledHeight * i, true
            )

            // Bind it
            framebuffer.bindFramebuffer(false)

            // GL options
            GlStateManager.matrixMode(5889)
            GlStateManager.loadIdentity()
            GlStateManager.ortho(
                0.0, scaledResolution.scaledWidth.toDouble(),
                scaledResolution.scaledHeight.toDouble(), 0.0, 1000.0, 3000.0
            )
            GlStateManager.matrixMode(5888)
            GlStateManager.loadIdentity()
            GlStateManager.translate(0.0f, 0.0f, -2000.0f)
        }

        // GL options
        GlStateManager.disableLighting()
        GlStateManager.disableFog()
        GlStateManager.disableDepth()
        GlStateManager.enableTexture2D()

        // Get width and height
        val width = scaledResolution.scaledWidth
        val height = scaledResolution.scaledHeight

        // Bind the background texture and render it
        tm.bindTexture(EnumBackground.BACKGROUND_1.location)
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0f, 0f, width, height, width.toFloat(), height.toFloat())

        // Get medium font
        val mediumFont = ZeriteFonts.medium

        // Draw zerite text
        var s = "ZERITE"
        ZeriteFonts.title.drawCenteredString(
            s, width / 2,
            height / 2 - ZeriteFonts.title.getHeight(s) * 2,
            Color(255, 255, 255, opacity).rgb
        )

        // Draw slogan
        s = "Minecraft, refined."
        mediumFont.drawCenteredString(
            s, width / 2,
            height / 2 - ZeriteFonts.title.getHeight(s) / 2,
            Color(255, 255, 255, opacity).rgb
        )

        // Translate the screen
        val translateOffset = (255 - opacity) / 255.0 * (height / 4.0)
        GlStateManager.translate(0.0, translateOffset, 0.0)

        // Define values
        val maxSize = 6.0

        // GL options
        GlStateManager.enableTexture2D()

        // Draw rectangles
        ShapeUtil.drawFilledRoundedRectangle(
            width / 6, (height * 0.8).toInt(), width / 6 * 4, 20, 3,
            Color(255, 255, 255, opacity).rgb
        )
        ShapeUtil.drawFilledRoundedRectangle(
            width / 6, (height * 0.8).toInt(),
            (progress / maxSize * (width / 6 * 4)).toInt(), 20, 3,
            Color(200, 200, 200, opacity).rgb
        )

        mediumFont.drawCenteredString(
            text, width / 2,
            (height * 0.8 + mediumFont.getHeight(text) / 3).toInt(),
            Color(0, 0, 0, opacity).rgb
        )

        // Reverse the translation
        GlStateManager.translate(0.0, -translateOffset, 0.0)

        // GL options
        GlStateManager.disableLighting()
        GlStateManager.disableFog()
        if (!overlay) {
            // Unbind framebuffer and render
            framebuffer!!.unbindFramebuffer()
            framebuffer.framebufferRender(
                scaledResolution.scaledWidth * i,
                scaledResolution.scaledHeight * i
            )

            // Update display
            Minecraft.getMinecraft().updateDisplay()
        }

        // GL options
        GlStateManager.enableAlpha()
        GlStateManager.alphaFunc(516, 0.1f)
    }

    fun updateData(text: String) {
        this.text = text
        progress++
        drawSplash(Minecraft.getMinecraft().textureManager, false, 255)
    }

}