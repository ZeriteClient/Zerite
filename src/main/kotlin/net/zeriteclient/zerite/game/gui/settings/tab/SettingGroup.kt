package net.zeriteclient.zerite.game.gui.settings.tab

import net.minecraft.util.ResourceLocation
import net.zeriteclient.zerite.game.tools.font.ZeriteFonts
import net.zeriteclient.zerite.util.game.MouseUtil
import net.zeriteclient.zerite.util.rendering.RenderDimension
import net.zeriteclient.zerite.util.rendering.ShapeUtil
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.max


class SettingGroup(val name: String, val icon: ResourceLocation) {

    val dropDowns: ArrayList<SettingDropdown> = arrayListOf()
    var dimension: RenderDimension = RenderDimension(0, 0, 0, 0)
    private var scrollProgress = 0
    private var lastYPos = 0

    fun draw() {
        val mediumFont = ZeriteFonts.medium

        mediumFont.drawString(name, dimension.x, dimension.y + 5, -0x1)

        GL11.glEnable(GL11.GL_STENCIL_TEST)
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT)

        GL11.glColorMask(false, false, false, false)
        GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 255)
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE)

        ShapeUtil.drawFilledRoundedRectangle(
            dimension.x,
            dimension.y + 20,
            dimension.width,
            dimension.height - 20,
            5,
            Color(255, 255, 255, 255).rgb
        )

        GL11.glColorMask(true, true, true, true)
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE)
        GL11.glStencilFunc(GL11.GL_EQUAL, 1, 255)

        ShapeUtil.drawGradientRect(
            dimension.x.toDouble(),
            dimension.y + 20.0,
            dimension.width.toDouble(),
            dimension.height - 20.0,
            Color(3, 169, 244, 100).rgb,
            Color(2, 136, 209, 255).rgb
        )

        var yPos = dimension.y + 20 - scrollProgress

        for (dropdown in dropDowns) {
            dropdown.dimension = RenderDimension(dimension.width, 0, dimension.x, yPos)
            dropdown.draw()

            yPos += dropdown.dimension.height
        }

        lastYPos = yPos

        GL11.glDisable(GL11.GL_STENCIL_TEST)
    }

    fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        dropDowns.forEach { it.mouseClicked(mouseX, mouseY, mouseButton) }
    }

    fun mouseScrolled(wheel: Int) {
        if (MouseUtil.isHovered(dimension.x, dimension.y, dimension.width, dimension.height))
            if (lastYPos - dimension.height > dimension.y || wheel < 0)
                scrollProgress = max(0, scrollProgress + wheel / 8)
    }

}