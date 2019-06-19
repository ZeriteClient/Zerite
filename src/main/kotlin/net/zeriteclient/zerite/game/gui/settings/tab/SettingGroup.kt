package net.zeriteclient.zerite.game.gui.settings.tab

import net.minecraft.util.ResourceLocation
import net.zeriteclient.zerite.game.tools.font.ZeriteFonts
import net.zeriteclient.zerite.util.rendering.RenderDimension
import net.zeriteclient.zerite.util.rendering.ShapeUtil
import java.awt.Color

class SettingGroup(val name: String, val icon: ResourceLocation, private val dropDowns: ArrayList<SettingDropdown>) {

    var dimension: RenderDimension = RenderDimension(0, 0, 0, 0)

    fun draw() {
        val mediumFont = ZeriteFonts.medium

        ShapeUtil.drawRectWithSize(dimension.x, dimension.y + 20, dimension.width, dimension.height - 20, Color(38, 38, 38, 255).rgb)

        mediumFont.drawString(name, dimension.x, dimension.y + 5, -0x1)

        var yPos = dimension.y + 20

        for (dropdown in dropDowns) {
            dropdown.dimension = RenderDimension(dimension.width, 0, dimension.x, yPos)
            dropdown.draw()

            yPos += dropdown.dimension.height
        }
    }

}