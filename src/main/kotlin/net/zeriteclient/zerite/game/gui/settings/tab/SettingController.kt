package net.zeriteclient.zerite.game.gui.settings.tab

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.zeriteclient.zerite.game.tools.font.ZeriteFonts
import net.zeriteclient.zerite.util.rendering.RenderDimension
import net.zeriteclient.zerite.util.rendering.ShapeUtil
import java.awt.Color

class SettingController(val groups: ArrayList<SettingGroup>, private var currentGroup: SettingGroup) {

    fun draw() {
        currentGroup = groups[0]

        val sr = ScaledResolution(Minecraft.getMinecraft())
        val width = sr.scaledWidth
        val height = sr.scaledHeight

        val mediumFont = ZeriteFonts.medium
        val mediumSmallFont = ZeriteFonts.mediumSmall
        val titleFont = ZeriteFonts.title

        ShapeUtil.drawRectWithSize(
            0, 0, width / 4, height,
            Color(65, 65, 65, 230).rgb
        )
        ShapeUtil.drawRectWithSize(
            0, 0, width / 4, 20,
            Color(0, 0, 0, 76).rgb
        )

        mediumFont.drawString("Zerite", 5, 5, -0x1)

        val radius = width / 32
        val spacing = radius * 3 - radius / 2
        var x = radius / 2
        var y = 30

        for (i in groups.indices) {
            val group = groups[i]

            ShapeUtil.drawFilledCircle(x + radius, y + radius, radius, 50, -0x1)
            mediumSmallFont
                .drawCenteredString(group.name, x + radius, y + radius * 2, -0x1)

            x += spacing

            if (i % 3 == 2) {
                x = radius / 2
                y += (spacing * 1.25).toInt()
            }
        }

        val rectX = width / 4 + 20
        val rectY = 20
        val rectWidth = width / 4 * 3 - 40
        val rectHeight = height - 40

        if (currentGroup == null) {
            ShapeUtil.drawFilledRoundedRectangle(
                rectX, rectY, rectWidth, rectHeight,
                5, Color(38, 38, 38, 255).rgb
            )

            titleFont.drawCenteredString(
                "Well this is awkward!",
                rectX + rectWidth / 2,
                rectY + rectHeight / 2 - 20,
                -0x1
            )
            mediumFont.drawCenteredString(
                "You haven't selected a category.",
                rectX + rectWidth / 2,
                rectY + rectHeight / 2 + 10,
                -0x1
            )
        } else {
            currentGroup.dimension = RenderDimension(rectWidth, rectHeight + 20, rectX, 0)
            currentGroup.draw()
        }
    }

}