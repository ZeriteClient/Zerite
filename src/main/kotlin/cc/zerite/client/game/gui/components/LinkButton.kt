package cc.zerite.client.game.gui.components

import cc.zerite.client.game.tools.font.ZeriteFonts
import cc.zerite.client.util.other.TimeUtil
import cc.zerite.client.util.rendering.ShapeUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.max
import kotlin.math.min

class LinkButton(buttonId: Int, x: Int, y: Int, buttonText: String) :
    GuiButton(buttonId, x, y, 20, 20, buttonText) {

    /**
     * Variables
     */
    private var hoverFade: Double = 0.0

    override fun drawButton(mc: Minecraft, mouseX: Int, mouseY: Int) {
        // Check if visible
        if (!visible) {
            return
        }

        // Get medium font
        val fontRenderer = ZeriteFonts.medium

        // Set sizes
        height = fontRenderer.size.toInt()
        width = fontRenderer.getWidth(displayString)

        // Create animation params
        hoverFade = if (hovered) min(255.0, hoverFade + TimeUtil.delta) else max(0.0, hoverFade - TimeUtil.delta)

        // GL options
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.blendFunc(770, 771)

        // Set hovered
        this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition
                && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height)
        this.mouseDragged(mc, mouseX, mouseY)

        // Draw string
        fontRenderer.drawString(displayString, xPosition, yPosition, -0x1)

        // Draw underline
        GL11.glLineWidth(2f)
        ShapeUtil.drawLine(
            xPosition, yPosition + height + 2, xPosition + width, yPosition + height + 2,
            Color(255, 255, 255, hoverFade.toInt()).rgb
        )
        GL11.glLineWidth(1f)

        // Enable blend and reset
        GlStateManager.enableBlend()
        GL11.glColor4f(1f, 1f, 1f, 1f)
    }

}