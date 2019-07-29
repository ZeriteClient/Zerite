package cc.zerite.client.game.gui.settings.tab

import cc.zerite.client.game.gui.components.custom.CustomWavedButton
import cc.zerite.client.game.tools.font.ZeriteFonts
import cc.zerite.client.util.rendering.RenderDimension
import cc.zerite.client.util.rendering.ShapeUtil
import cc.zerite.client.util.rendering.usingStencil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import java.awt.Color

class SettingController(private val groups: ArrayList<SettingGroup>) {

    private var currentGroup: SettingGroup? = null
    private var scrollProgress = 0
    private val headerButtons = arrayListOf(
        CustomWavedButton("GENERAL", smallFont = true, border = false),
        CustomWavedButton("COSMETICS", smallFont = true, border = false),
        CustomWavedButton("OTHER", smallFont = true, border = false)
    )

    private val wavedButtons = arrayListOf<CustomWavedButton>()

    fun draw() {
        if (currentGroup == null) {
            currentGroup = groups[0]
        }

        val sr = ScaledResolution(Minecraft.getMinecraft())
        val width = sr.scaledWidth
        val height = sr.scaledHeight

        val mediumFont = ZeriteFonts.medium
        val mediumSmallFont = ZeriteFonts.mediumSmall
        val titleFont = ZeriteFonts.title

        titleFont.drawCenteredString("Zerite", width / 2, height / 12, -0xF)

        val left = width / 8
        val top = height / 4
        val bWidth = width / 4 * 3
        val bHeight = height / 3 * 2
        val topHeight = 30
        val sideWidth = bWidth / 3
        val profileHeight = 20

        usingStencil {
            stencil = {
                ShapeUtil.drawFilledRoundedRectangle(left, top, bWidth, topHeight, 3, -0xF)
                ShapeUtil.drawRectWithSize(left, top + 5, bWidth, topHeight - 5, -0xF)
            }

            scene = {
                ShapeUtil.drawRectWithSize(left, top, bWidth, topHeight, Color(55, 71, 79, 255).rgb)

                val buttonWidth = 50
                val buttonHeight = 20
                for (i in headerButtons.indices) {
                    val button = headerButtons[i]
                    button.drawButton(
                        left + 5 + i * (buttonWidth + 5),
                        top + buttonHeight / 4,
                        buttonWidth,
                        buttonHeight
                    )
                }
            }
        }

        usingStencil {
            stencil = {
                ShapeUtil.drawRectWithSize(left, top + topHeight, sideWidth, profileHeight, -0xF)
            }

            scene = {
                ShapeUtil.drawRectWithSize(left, top + topHeight, sideWidth, profileHeight, Color(69, 90, 100, 255).rgb)

                mediumSmallFont.drawString("PROFILE", left + 5, top + topHeight + 5, -0xF)
            }
        }

        usingStencil {
            stencil = {
                ShapeUtil.drawFilledRoundedRectangle(
                    left,
                    top + topHeight + profileHeight,
                    sideWidth,
                    bHeight - topHeight - profileHeight,
                    3,
                    -0xF
                )
                ShapeUtil.drawRectWithSize(left, top + topHeight + profileHeight, 10, 10, -0xF)
                ShapeUtil.drawRectWithSize(
                    left + sideWidth - 10,
                    top + topHeight + profileHeight,
                    10,
                    bHeight - topHeight - profileHeight,
                    -0xF
                )
            }

            scene = {
                ShapeUtil.drawRectWithSize(
                    left,
                    top + topHeight + profileHeight,
                    sideWidth,
                    bHeight - topHeight - profileHeight,
                    Color(55, 71, 79, 255).rgb
                )

                val textHeight = 10
                for (i in groups.indices) {
                    val group = groups[i]
                    val y = top + topHeight + profileHeight + 5 + i * textHeight

                    val button = wavedButtons.firstOrNull { it.label == group.name }.let {
                        val button = it ?: CustomWavedButton(
                            group.name,
                            smallFont = true,
                            border = false,
                            radius = 0,
                            alignPadding = 5,
                            clicked = {
                                currentGroup = group
                            }
                        )
                        if (it == null) {
                            wavedButtons += button
                        }
                        button
                    }

                    button.drawButton(left, y, sideWidth, textHeight)
                }
            }
        }

        usingStencil {
            stencil = {
                ShapeUtil.drawFilledRoundedRectangle(
                    left + sideWidth,
                    top + topHeight,
                    bWidth - sideWidth,
                    bHeight - topHeight,
                    3,
                    -0xF
                )
                ShapeUtil.drawRectWithSize(left + sideWidth, top + topHeight, bWidth - sideWidth, 10, -0xF)
                ShapeUtil.drawRectWithSize(left + sideWidth, top + topHeight, 10, bHeight - topHeight, -0xF)
            }

            scene = {
                ShapeUtil.drawRectWithSize(
                    left + sideWidth,
                    top + topHeight,
                    bWidth - sideWidth,
                    bHeight - topHeight,
                    Color(38, 50, 56, 255).rgb
                )

                currentGroup?.dimension =
                    RenderDimension(bWidth - sideWidth, bHeight - topHeight, left + sideWidth, top + topHeight)
                currentGroup?.draw()
            }
        }

        ShapeUtil.drawRoundedRectangle(left, top, bWidth, bHeight, 3, 1.0f, -0xF)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
    }

    fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        headerButtons.forEach { it.mousePressed(mouseX, mouseY) }
        wavedButtons.forEach { it.mousePressed(mouseX, mouseY) }
        currentGroup?.mouseClicked(mouseX, mouseY, mouseButton)
    }

    fun mouseScrolled(wheel: Int) {
        currentGroup?.mouseScrolled(wheel)
    }

}