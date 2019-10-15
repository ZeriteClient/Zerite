package cc.zerite.client.util.rendering

import cc.zerite.client.game.options.GuiOptions
import cc.zerite.client.util.EnumBackground
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import org.lwjgl.opengl.GL11
import java.awt.Color

object GuiUtil {

    fun drawBackground(width: Int, height: Int, override: Boolean = false) {
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
        GL11.glDisable(GL11.GL_ALPHA_TEST)
        if (Minecraft.getMinecraft().theWorld == null || override) {
            EnumBackground.BACKGROUND_1.bind()
            Gui.drawModalRectWithCustomSizedTexture(
                0,
                0,
                0.0f,
                0.0f,
                width,
                height,
                width.toFloat(),
                height.toFloat()
            )
        }

        if (GuiOptions.blueGradient || override || Minecraft.getMinecraft().theWorld == null) {
            ShapeUtil.drawGradientRect(
                0.0,
                0.0,
                width.toDouble(),
                height.toDouble(),
                Color(3, 169, 244, 100).rgb,
                Color(2, 136, 209, 255).rgb
            )
        } else {
            ShapeUtil.drawGradientRect(0.0, 0.0, width.toDouble(), height.toDouble(), -1072689136, -804253680)
        }
    }
}