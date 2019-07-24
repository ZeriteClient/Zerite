package cc.zerite.client.game.gui.settings.element.impl

import cc.zerite.client.game.gui.settings.element.SettingsElement
import cc.zerite.client.game.tools.font.ZeriteFonts
import cc.zerite.client.util.game.MouseUtil
import cc.zerite.client.util.rendering.ShapeUtil

class SettingsButton(private val label: String, private val onClick: () -> Unit) : SettingsElement() {

    /**
     * Draw the button to the screen
     */
    override fun draw() {
        dimensions.height = 15

        val mediumSmallFont = ZeriteFonts.mediumSmall
        mediumSmallFont.drawString(label, dimensions.x, dimensions.y + 3, -0x1)

        ShapeUtil.drawRoundedRectangle(
            dimensions.x + dimensions.width - 60,
            dimensions.y + 1,
            60,
            dimensions.height - 2,
            3,
            2.0f,
            -0x1
        )
        mediumSmallFont.drawCenteredString("ACTIVATE", dimensions.x + dimensions.width - 32, dimensions.y + 3, -0x1)
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
            onClick()
        }
    }

}