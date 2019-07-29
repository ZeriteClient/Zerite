package cc.zerite.client.game.gui.settings.tab

import cc.zerite.client.game.gui.settings.element.SettingsElement
import cc.zerite.client.game.tools.font.ZeriteFonts
import cc.zerite.client.util.rendering.RenderDimension
import com.github.fcannizzaro.material.Colors
import kotlin.math.max

class SettingDropdown(val name: String) {

    val elements: ArrayList<SettingsElement> = arrayListOf()
    var dimension: RenderDimension = RenderDimension(0, 0, 0, 0)

    fun draw() {
        val mediumSmallFont = ZeriteFonts.mediumSmall

        mediumSmallFont.drawString(
            name.toUpperCase(),
            dimension.x + 5,
            dimension.y + 5,
            Colors.blue_grey_700.asColor().rgb
        )

        var xPos = dimension.x + 10
        var yPos = dimension.y + 16
        val itemWidth = dimension.width / 2 - 20

        var lastHeight = 0

        for (i in elements.indices) {
            val element = elements[i]
            element.dimensions = RenderDimension(itemWidth, 0, xPos, yPos)

            lastHeight = element.dimensions.height

            element.draw()

            if (lastHeight == 0) {
                lastHeight = element.dimensions.height
            }

            xPos += itemWidth + 20

            if (i % 2 == 1) {
                xPos = dimension.x + 10
                yPos += max(lastHeight, element.dimensions.height) + 5
            }
        }

        if (elements.size % 2 == 0)
            yPos -= lastHeight + 5

        dimension.height = yPos + lastHeight - dimension.y + 2
    }

    fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        elements.forEach { it.mouseClicked(mouseButton, mouseX, mouseY) }
    }

}