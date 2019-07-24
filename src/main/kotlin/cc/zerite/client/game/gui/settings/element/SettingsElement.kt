package cc.zerite.client.game.gui.settings.element

import cc.zerite.client.util.rendering.RenderDimension

abstract class SettingsElement {

    var dimensions: RenderDimension = RenderDimension(0, 0, 0, 0)

    abstract fun draw()
    abstract fun mouseClicked(button: Int, mouseX: Int, mouseY: Int)

    open fun mouseDragged(mouseX: Int, mouseY: Int) {}
    open fun mouseReleased(mouseX: Int, mouseY: Int) {}

}