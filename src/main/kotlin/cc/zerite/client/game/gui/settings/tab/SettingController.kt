package cc.zerite.client.game.gui.settings.tab

import cc.zerite.client.game.gui.components.custom.CustomWavedButton
import cc.zerite.client.game.tools.font.ZeriteFonts
import cc.zerite.client.util.game.MouseUtil
import cc.zerite.client.util.rendering.ResolutionUtil
import cc.zerite.client.util.rendering.ShapeUtil
import cc.zerite.client.util.rendering.usingStencil
import com.github.fcannizzaro.material.Colors
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import java.awt.Color
import kotlin.math.max

class SettingController(private val groups: ArrayList<SettingGroup>) {

    private var currentGroup: SettingGroup? = null
    private var scrollProgress = 0
    private val headerButtons = arrayListOf(
        CustomWavedButton("GENERAL"),
        CustomWavedButton("COSMETICS"),
        CustomWavedButton("OTHER")
    )

    fun draw() {
        val sr = ScaledResolution(Minecraft.getMinecraft())
        val width = sr.scaledWidth
        val height = sr.scaledHeight

        val mediumFont = ZeriteFonts.medium
        val mediumSmallFont = ZeriteFonts.mediumSmall
        val titleFont = ZeriteFonts.title

        titleFont.drawCenteredString("Zerite", width / 2, height / 12, -0xF)

        val left = width / 6
        val top = height / 5
        val bWidth = width / 3 * 2
        val bHeight = height / 3 * 2
        val topHeight = 20

        ShapeUtil.drawRoundedRectangle(left, top, bWidth, bHeight, 5, 1.0f, -0xF)
        ShapeUtil.drawLine(left + width / 4, top, left + width / 4, top + bHeight, Colors.grey_600.asColor().rgb)

        usingStencil {
            stencil = {
                ShapeUtil.drawFilledRoundedRectangle(left, top, 15, bHeight, 5, -0x0)
                ShapeUtil.drawRectWithSize(left + 10, top, width / 3 - 10, bHeight, -0x0)
            }

            scene = {
                ShapeUtil.drawRectWithSize(left, top + topHeight, width / 4, bHeight - topHeight, Color(100, 100, 100, 100).rgb)
                ShapeUtil.drawRectWithSize(left, top, width / 4, topHeight, Color(150, 150, 150, 100).rgb)

                ShapeUtil.drawLine(left, top + topHeight, left + width / 4, top + topHeight, Colors.grey_600.asColor().rgb)

                mediumSmallFont.drawString("PROFILE", left + 6, top + (topHeight - mediumSmallFont.getHeight("PROFILE")) / 2, -0xF)

                for (i in groups.indices) {
                    val group = groups[i]

                    val gLeft = left + 6
                    val gTop = top + topHeight + 6 + i * 10

                    mediumSmallFont.drawString(group.name.toUpperCase(), gLeft, gTop, Color.WHITE.rgb)
                }
            }
        }

        usingStencil {
            stencil = {
                ShapeUtil.drawFilledRoundedRectangle(left + width / 2, top, 15, bHeight, 5, -0x0)
                ShapeUtil.drawRectWithSize(left + width / 6, top, width / 2 - 10, bHeight, -0x0)
            }

            scene = {
                ShapeUtil.drawRectWithSize(left + width / 4, top + topHeight, width / 2, bHeight - topHeight, Color(100, 100, 100, 100).rgb)
                ShapeUtil.drawRectWithSize(left + width / 4, top, width / 2, topHeight, Color(150, 150, 150, 100).rgb)

                headerButtons.forEachIndexed { index, it ->
                    val x = left + width / 2 + 6 + index * 40
                    val y = top + (topHeight + 10) / 2

                    it.drawButton(x, y + 5, 30, 10)
                }
            }
        }

//        val rectX = width / 4 + 20.0
//        val rectY = 20.0
//        val rectWidth = width / 4 * 3 - 40.0
//        val rectHeight = height - 50.0
//
//        if (currentGroup == null) {
//            usingStencil {
//                stencil = {
//                    ShapeUtil.drawFilledRoundedRectangle(
//                        rectX.toInt(),
//                        rectY.toInt(),
//                        rectWidth.toInt(),
//                        rectHeight.toInt(),
//                        5,
//                        -0x1
//                    )
//                }
//
//                scene = {
//                    ShapeUtil.drawGradientRect(
//                        rectX,
//                        rectY,
//                        rectWidth,
//                        rectHeight,
//                        Color(3, 169, 244, 100).rgb,
//                        Color(2, 136, 209, 255).rgb
//                    )
//
//                    titleFont.drawCenteredString(
//                        "Well this is awkward!",
//                        (rectX + rectWidth / 2).toInt(),
//                        (rectY + rectHeight / 2 - 20).toInt(),
//                        -0x1
//                    )
//                    mediumFont.drawCenteredString(
//                        "You haven't selected a category.",
//                        (rectX + rectWidth / 2).toInt(),
//                        (rectY + rectHeight / 2 + 10).toInt(),
//                        -0x1
//                    )
//                }
//            }
//        } else {
//            currentGroup!!.dimension = RenderDimension(rectWidth.toInt(), (rectHeight + 20).toInt(), rectX.toInt(), 0)
//            currentGroup!!.draw()
//        }
    }

    fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        headerButtons.forEach { it.mousePressed(mouseX, mouseY) }

        val sr = ScaledResolution(Minecraft.getMinecraft())
        val width = sr.scaledWidth

        val radius = width / 32
        val spacing = radius * 3 - radius / 2
        var x = radius / 2
        var y = 30 - scrollProgress

        for (i in groups.indices) {
            if (MouseUtil.isHovered(x, y, radius * 2, radius * 2)) {
                currentGroup = groups[i]
            }

            x += spacing

            if (i % 3 == 2) {
                x = radius / 2
                y += (spacing * 1.25).toInt()
            }
        }

        currentGroup?.mouseClicked(mouseX, mouseY, mouseButton)
    }

    fun mouseScrolled(wheel: Int) {
        val sr = ResolutionUtil.scaledResolution

        if (MouseUtil.isHovered(0, 0, sr.scaledWidth / 4, sr.scaledHeight)) {
            val radius = sr.scaledWidth / 32
            val spacing = radius * 3 - radius / 2
            var x = radius / 2
            var y = 30 - scrollProgress - wheel / 8

            for (i in groups.indices) {
                x += spacing

                if (i % 3 == 2) {
                    x = radius / 2
                    y += (spacing * 1.25).toInt()
                }
            }

            if (y <= 30)
                return

            scrollProgress = max(0, scrollProgress + wheel / 8)
        }

        currentGroup?.mouseScrolled(wheel)
    }

}