package net.zeriteclient.zerite.game.gui.settings.element.impl

import net.zeriteclient.zerite.game.gui.settings.element.SettingsElement
import net.zeriteclient.zerite.game.tools.font.ZeriteFonts
import net.zeriteclient.zerite.util.game.MouseUtil
import net.zeriteclient.zerite.util.rendering.AnimationUtil
import net.zeriteclient.zerite.util.rendering.ShapeUtil
import java.awt.Color

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
        animation = Math.max(
            -50.0,
            Math.min(50.0, AnimationUtil.easeOut(lastAnimation, if (toggled) 50.0 else -50.0, lastChange, 1000.0))
        )
        animation = if (animation < 0) animation + 50 else animation

        dimensions.height = 15

        val mediumSmallFont = ZeriteFonts.mediumSmall
        mediumSmallFont.drawString(label, dimensions.x, dimensions.y + 3, -0x1)

        ShapeUtil.drawFilledRoundedRectangle(
            dimensions.x + dimensions.width - 25,
            dimensions.y + 4,
            15,
            6,
            2,
            -0x1
        )
        ShapeUtil.drawFilledCircle(
            (dimensions.x + dimensions.width - 32 + ((animation + 50) / 100) * 20).toInt(),
            dimensions.y + dimensions.height / 2,
            5,
            50,
            Color.getHSBColor(((animation + 50.0f) / 100.0f * 0.35f - 0.13f).toFloat(), 0.8f, 0.8f).rgb
        )
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