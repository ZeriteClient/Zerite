package cc.zerite.client.game.gui.components

import cc.zerite.client.util.other.TimeUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import org.newdawn.slick.Color
import kotlin.math.max
import kotlin.math.min

class IconButton(
    buttonId: Int,
    x: Int,
    y: Int,
    widthIn: Int = 20,
    heightIn: Int = 20,
    private val location: ResourceLocation
) : GuiButton(buttonId, x, y, widthIn, heightIn, "") {

    /**
     * Variables
     */
    private var hoverFade: Double = 0.0

    override fun drawButton(mc: Minecraft, mouseX: Int, mouseY: Int) {
        // Check if visible
        if (!visible) {
            return
        }

        // Animation
        hoverFade = if (hovered) min(30.0, hoverFade + TimeUtil.delta / 6) else max(0.0, hoverFade - TimeUtil.delta / 6)

        // GL options
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

        // Set hovered
        this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition
                && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height)
        this.mouseDragged(mc, mouseX, mouseY)

        // Create fade & bind
        val fade = if (enabled) (255 - hoverFade).toInt() else 200
        Color(fade, fade, fade, 255).bind()

        // Bind texture
        Minecraft.getMinecraft().textureManager.bindTexture(location)

        // Enable 2D texture
        GlStateManager.enableTexture2D()

        // Draw icon
        Gui.drawModalRectWithCustomSizedTexture(
            xPosition,
            yPosition,
            0f,
            0f,
            width,
            height,
            width.toFloat(),
            height.toFloat()
        )

        // Reset colors
        GL11.glColor4f(1f, 1f, 1f, 1f)
    }

}