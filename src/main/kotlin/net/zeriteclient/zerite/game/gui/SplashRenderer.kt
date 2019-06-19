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
        GlStateManager.enableBlend()

        // Get width and height
        val width = scaledResolution.scaledWidth
        val height = scaledResolution.scaledHeight

        // Bind the background texture and render it
        GlStateManager.color(1.0f, 1.0f, 1.0f, opacity / 255.0f)
        tm.bindTexture(EnumBackground.BACKGROUND_1.location)
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0f, 0f, width, height, width.toFloat(), height.toFloat())

        ShapeUtil.drawGradientRect(
            0.0,
            0.0,
            width.toDouble(),
            height.toDouble(),
            Color(3, 169, 244, Math.max(0, opacity - 100)).rgb,
            Color(2, 136, 209, opacity).rgb
        )

        // Get medium font
        val mediumFont = ZeriteFonts.medium

        // Draw zerite text
        var s = "ZERITE"
        ZeriteFonts.titleLarge.drawCenteredString(
            s, width / 2,
            height / 2 - 10 - (ZeriteFonts.titleLarge.getHeight(s) / 1.5).toInt(),
            Color(255, 255, 255, opacity).rgb
        )

        // Draw slogan
        s = "Minecraft, refined."
        mediumFont.drawCenteredString(
            s, width / 2,
            height / 2 - 10 + ZeriteFonts.titleLarge.getHeight(s) / 4 + 5,
            Color(255, 255, 255, opacity).rgb
        )

        // Translate the screen
//        val translateOffset = (255 - opacity) / 255.0 * (height / 4.0)
//        GlStateManager.translate(0.0, translateOffset, 0.0)

        // Define values
        val maxSize = 6.0
        val whiteColor = Color(255, 255, 255, opacity).rgb

        // GL options
        GlStateManager.enableTexture2D()

        // Draw rectangles
        ShapeUtil.drawRectWithSize(0, height - 10, width, 10, whiteColor)
        ShapeUtil.drawRectWithSize(
            0,
            height - 10,
            (progress / maxSize * width).toInt(),
            10,
            Color(41,182,246, opacity).rgb
        )

        s = "Stage 1"
        mediumFont.drawString(s, 5, height - 15 - mediumFont.getHeight(s) * 2, whiteColor)

        s = "Loading Minecraft"
        ZeriteFonts.regular.drawString(s, 5, height - 15 - ZeriteFonts.regular.getHeight(s), whiteColor)

        s = "${(progress / maxSize * 100).toInt()}% complete"
        mediumFont.drawString(s, width - 5 - mediumFont.getWidth(s), height - 15 - mediumFont.getHeight(s), whiteColor)

        // Reverse the translation
//        GlStateManager.translate(0.0, -translateOffset, 0.0)

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