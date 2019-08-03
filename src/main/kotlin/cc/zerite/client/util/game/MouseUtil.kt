package cc.zerite.client.util.game

import cc.zerite.client.util.rendering.ResolutionUtil
import net.minecraft.client.Minecraft
import org.lwjgl.input.Mouse
import java.awt.Point
import java.awt.Rectangle

object MouseUtil {

    private val mousePosition: Point
        get() {
            val scaledResolution = ResolutionUtil.scaledResolution
            val width = Mouse.getEventX() * scaledResolution.scaledWidth / Minecraft
                .getMinecraft().displayWidth
            val height = (scaledResolution.scaledHeight
                    - Mouse.getEventY() * scaledResolution.scaledHeight / Minecraft
                .getMinecraft().displayHeight - 1)
            return Point(width, height)
        }

    fun isHovered(x: Int, y: Int, width: Int, height: Int): Boolean = Rectangle(x, y, width, height).contains(
        mousePosition
    )

}