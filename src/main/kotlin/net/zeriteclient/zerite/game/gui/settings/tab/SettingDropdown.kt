package net.zeriteclient.zerite.game.gui.settings.tab

import net.zeriteclient.zerite.game.gui.settings.element.SettingsElement
import net.zeriteclient.zerite.game.tools.font.ZeriteFonts
import net.zeriteclient.zerite.util.rendering.RenderDimension
import net.zeriteclient.zerite.util.rendering.ShapeUtil
import java.awt.Color

class SettingDropdown(private val name: String, private val elements: ArrayList<SettingsElement>) {

    var dimension: RenderDimension = RenderDimension(0, 0, 0, 0)

    fun draw() {
        val mediumSmallFont = ZeriteFonts.mediumSmall

        ShapeUtil.drawRectWithSize(dimension.x, dimension.y, dimension.width, 20, Color(255, 255, 255, 50).rgb)
        mediumSmallFont.drawString(name, dimension.x + 5, dimension.y + 5, -0x1)

        var xPos = dimension.x + 5
        var yPos = dimension.y + 22
        val itemWidth = dimension.width / 2 - 10

        var lastHeight = 0

        for (i in elements.indices) {
            val element = elements[i]
            element.dimensions = RenderDimension(itemWidth, 0, xPos, yPos)

            lastHeight = element.dimensions.height

            element.draw()

            if (lastHeight == 0) {
                lastHeight = element.dimensions.height
            }

            xPos += itemWidth + 10

            if (i % 2 == 1) {
                xPos = dimension.x + 5
                yPos += Math.max(lastHeight, element.dimensions.height) + 5
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