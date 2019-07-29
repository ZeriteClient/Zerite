package cc.zerite.client.game.gui.settings.element.impl

import cc.zerite.client.game.gui.settings.element.SettingsElement
import cc.zerite.client.game.tools.font.ZeriteFonts
import cc.zerite.client.util.game.MouseUtil
import cc.zerite.client.util.rendering.AnimationUtil
import cc.zerite.client.util.rendering.ShapeUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.max
import kotlin.math.min

class SettingsToggle(
    private val label: String,
    private val onChange: (new: Boolean) -> Unit,
    private var toggled: Boolean = false
) : SettingsElement() {

    private var animation = 0.0
    private var lastAnimation = 0.0
    private var lastChange = System.currentTimeMillis()

    /**
     * Draw the button to the screen
     */
    override fun draw() {
        animation = max(
            -50.0,
            min(50.0, AnimationUtil.easeOut(lastAnimation, if (toggled) 50.0 else -50.0, lastChange, 500.0))
        )
        animation = if (animation < 0) animation + 50 else animation

        dimensions.height = 15

        val mediumSmallFont = ZeriteFonts.mediumSmall
        mediumSmallFont.drawString(label.toUpperCase(), dimensions.x, dimensions.y + 3, -0x1)

        val x = dimensions.x + mediumSmallFont.getWidth(label.toUpperCase()) + 10
        val y = dimensions.y + dimensions.height / 4
        val height = dimensions.height / 4 * 3

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)

        Minecraft.getMinecraft().textureManager.bindTexture(ResourceLocation("textures/icons/close.png"))
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, height, height, height.toFloat(), height.toFloat())

        ShapeUtil.drawRoundedRectangle(x, y, height, height, 3, 2f, Color.WHITE.rgb)

        ShapeUtil.drawFilledRoundedRectangle(
            x,
            y,
            height,
            height,
            3,
            Color(3, 155, 229, (animation / 50 * 255).toInt()).rgb
        )

        GL11.glColor4f(1.0f, 1.0f, 1.0f, (animation / 50.0f).toFloat())
        Minecraft.getMinecraft().textureManager.bindTexture(ResourceLocation("textures/icons/check.png"))
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, height, height, height.toFloat(), height.toFloat())

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
    }

    /**
     * Invoked when the left click button is clicked
     *
     * @param button the mouse button being clicked
     * @param mouseX the current mouse x location
     * @param mouseY the current mouse y location
     */
    override fun mouseClicked(button: Int, mouseX: Int, mouseY: Int) {
        /* If the mouse button isn't left click, return */
        if (button != 0) {
            return
        }

        /*
         * If the button is hovered over and it's clicked
         * Do what the button is told to do
         */
        if (MouseUtil.isHovered(dimensions.x, dimensions.y, dimensions.width, dimensions.height)) {
            lastAnimation = animation
            lastChange = System.currentTimeMillis()
            toggled = !toggled
            onChange(toggled)
        }
    }

}