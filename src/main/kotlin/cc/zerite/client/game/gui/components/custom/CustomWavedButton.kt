package cc.zerite.client.game.gui.components.custom

import cc.zerite.client.game.entities.WaveData
import cc.zerite.client.game.tools.font.ZeriteFonts
import cc.zerite.client.util.game.MouseUtil
import cc.zerite.client.util.other.TimeUtil
import cc.zerite.client.util.rendering.AnimationUtil
import cc.zerite.client.util.rendering.GLUtil
import cc.zerite.client.util.rendering.RenderDimension
import cc.zerite.client.util.rendering.ShapeUtil
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.max
import kotlin.math.min

class CustomWavedButton(
    var label: String
) {

    private var dimension = RenderDimension(0, 0, 0, 0)
    private val waveData = arrayListOf<WaveData>()

    var hoverFade = 0.0
    var enabled = true

    fun drawButton(xPosition: Int, yPosition: Int, width: Int, height: Int) {
        dimension = RenderDimension(width, height, xPosition, yPosition)

        hoverFade =
            if (MouseUtil.isHovered(xPosition, yPosition, width, height)) min(
                90.0,
                hoverFade + TimeUtil.delta / 2f
            ) else max(40.0, hoverFade - TimeUtil.delta / 2f)

        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0)
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

        ShapeUtil.drawFilledRoundedRectangle(
            xPosition, yPosition, width, height, 3,
            if (!this.enabled)
                Color(0, 0, 0, 100).rgb
            else
                Color(255, 255, 255, hoverFade.toInt() / 2).rgb
        )
        ShapeUtil.drawRoundedRectangle(
            xPosition, yPosition, width, height, 3, 2f,
            if (!this.enabled)
                Color(0, 0, 0, 100).rgb
            else
                Color(255, 255, 255, hoverFade.toInt()).rgb
        )

        GLUtil.preStencil()
        ShapeUtil.drawFilledRoundedRectangle(xPosition, yPosition, width, height, 3, -0x1)
        GLUtil.postStencil()

        waveData.removeIf { data ->
            val x = data.x
            val y = data.y
            val animation = AnimationUtil.easeOut(0.0, 1.0, data.startTime, 1000.0, 2.0)

            ShapeUtil.drawFilledCircle(
                x, y, (animation * width / 2).toInt(), 50, Color(
                    255,
                    255,
                    255,
                    (130 - animation * 130).toInt()
                ).rgb
            )

            data.startTime + 1000 < System.currentTimeMillis()
        }

        GLUtil.postStencilDraw()

        GL11.glColor4f(1f, 1f, 1f, 1f)
        ZeriteFonts.medium
            .drawCenteredString(
                label.toUpperCase(),
                xPosition + width / 2, yPosition + (height - ZeriteFonts.medium
                    .getHeight(label.toUpperCase())) / 2,
                Color(-0x1).rgb
            )
    }

    fun mousePressed(mouseX: Int, mouseY: Int) {
        if (MouseUtil.isHovered(dimension.x, dimension.y, dimension.width, dimension.height)) {
            waveData += WaveData(mouseX, mouseY, System.currentTimeMillis())
        }
    }

}