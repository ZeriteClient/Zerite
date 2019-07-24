package cc.zerite.client.game.gui.settings.tab

import cc.zerite.client.game.tools.font.ZeriteFonts
import cc.zerite.client.util.game.MouseUtil
import cc.zerite.client.util.rendering.RenderDimension
import cc.zerite.client.util.rendering.ResolutionUtil
import cc.zerite.client.util.rendering.ShapeUtil
import cc.zerite.client.util.rendering.usingStencil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.max

class SettingController(private val groups: ArrayList<SettingGroup>) {

    private var currentGroup: SettingGroup? = null
    private var scrollProgress = 0

    fun draw() {
        val sr = ScaledResolution(Minecraft.getMinecraft())
        val width = sr.scaledWidth
        val height = sr.scaledHeight

        val mediumFont = ZeriteFonts.medium
        val mediumSmallFont = ZeriteFonts.mediumSmall
        val titleFont = ZeriteFonts.title

        ShapeUtil.drawGradientRect(
            0.0,
            0.0,
            width / 4.0,
            height.toDouble(),
            Color(3, 169, 244, 100).rgb,
            Color(2, 136, 209, 255).rgb
        )
        ShapeUtil.drawRectWithSize(0, 0, width / 4, 20, Color(255, 255, 255, 50).rgb)

        val tessellator = Tessellator.getInstance()
        val worldRenderer = tessellator.worldRenderer

        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1)
        GlStateManager.disableAlpha()
        GlStateManager.shadeModel(7425)
        GlStateManager.disableTexture2D()

        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR)
        worldRenderer.pos(0.0, 20.0, 0.0).tex(0.0, 0.0).color(0, 0, 0, 30).endVertex()
        worldRenderer.pos(0.0, 24.0, 0.0).tex(0.0, 0.0).color(0, 0, 0, 0).endVertex()
        worldRenderer.pos(width / 4.0, 24.0, 0.0).tex(0.0, 0.0).color(0, 0, 0, 0).endVertex()
        worldRenderer.pos(width / 4.0, 20.0, 0.0).tex(0.0, 0.0).color(0, 0, 0, 30).endVertex()
        tessellator.draw()

        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR)
        worldRenderer.pos(width / 4.0, 0.0, 0.0).tex(0.0, 0.0).color(0, 0, 0, 30).endVertex()
        worldRenderer.pos(width / 4.0 + 3.0, 0.0, 0.0).tex(0.0, 0.0).color(0, 0, 0, 0).endVertex()
        worldRenderer.pos(width / 4.0 + 3.0, height.toDouble(), 0.0).tex(0.0, 0.0).color(0, 0, 0, 0).endVertex()
        worldRenderer.pos(width / 4.0, height.toDouble(), 0.0).tex(0.0, 0.0).color(0, 0, 0, 30).endVertex()
        tessellator.draw()

        mediumFont.drawString("Zerite", 5, 5, -0x1)

        val radius = width / 32
        val spacing = radius * 3 - radius / 2
        var x = radius / 2
        var y = 30 - scrollProgress

        usingStencil {
            stencil = {
                ShapeUtil.drawRectWithSize(0, 20, width / 4, height - 20, -0xF)
            }

            scene = {
                for (i in groups.indices) {
                    val group = groups[i]

                    ShapeUtil.drawFilledCircle(x + radius, y + radius, radius, 70, -0x1)

                    GL11.glColor4f(0.2f, 0.2f, 0.2f, 1.0f)
                    Minecraft.getMinecraft().textureManager.bindTexture(group.icon)
                    Gui.drawModalRectWithCustomSizedTexture(
                        x + radius / 4,
                        y + radius / 4,
                        0.0f,
                        0.0f,
                        (radius * 1.5).toInt(),
                        (radius * 1.5).toInt(),
                        radius * 1.5f,
                        radius * 1.5f
                    )

                    mediumSmallFont
                        .drawCenteredString(group.name, x + radius, y + radius * 2, -0x1)

                    x += spacing

                    if (i % 3 == 2) {
                        x = radius / 2
                        y += (spacing * 1.25).toInt()
                    }
                }
            }
        }

        val rectX = width / 4 + 20.0
        val rectY = 20.0
        val rectWidth = width / 4 * 3 - 40.0
        val rectHeight = height - 50.0

        if (currentGroup == null) {
            usingStencil {
                stencil = {
                    ShapeUtil.drawFilledRoundedRectangle(
                        rectX.toInt(),
                        rectY.toInt(),
                        rectWidth.toInt(),
                        rectHeight.toInt(),
                        5,
                        -0x1
                    )
                }

                scene = {
                    ShapeUtil.drawGradientRect(
                        rectX,
                        rectY,
                        rectWidth,
                        rectHeight,
                        Color(3, 169, 244, 100).rgb,
                        Color(2, 136, 209, 255).rgb
                    )

                    titleFont.drawCenteredString(
                        "Well this is awkward!",
                        (rectX + rectWidth / 2).toInt(),
                        (rectY + rectHeight / 2 - 20).toInt(),
                        -0x1
                    )
                    mediumFont.drawCenteredString(
                        "You haven't selected a category.",
                        (rectX + rectWidth / 2).toInt(),
                        (rectY + rectHeight / 2 + 10).toInt(),
                        -0x1
                    )
                }
            }
        } else {
            currentGroup!!.dimension = RenderDimension(rectWidth.toInt(), (rectHeight + 20).toInt(), rectX.toInt(), 0)
            currentGroup!!.draw()
        }
    }

    fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
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