package cc.zerite.client.game.tools.notification

import cc.zerite.client.event.RenderGameOverlayEvent
import cc.zerite.client.event.Subscribe
import cc.zerite.client.game.tools.font.ZeriteFonts
import cc.zerite.client.injection.bootstrap.impl.annotations.Instance
import cc.zerite.client.util.other.StringUtil
import cc.zerite.client.util.other.TimeUtil
import cc.zerite.client.util.rendering.AnimationUtil
import cc.zerite.client.util.rendering.EasingUtil
import cc.zerite.client.util.rendering.ShapeUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11

@Instance(registerEvents = true)
object NotificationManager {

    private val notificationList: ArrayList<Notification> = arrayListOf()
    private var deltaY: Double = 0.0
    private var lastMovement: Long = System.currentTimeMillis()
    private var lastYTotal: Double = 0.0

    @Subscribe
    private fun onRenderGameOverlay(e: RenderGameOverlayEvent) {
        val delta = TimeUtil.delta / 20.0
        val regularFont = ZeriteFonts.regular

        deltaY -= EasingUtil.easeOut(
            System.currentTimeMillis().toFloat(), 0.0f,
            (delta / 20 * (deltaY + 2)).toFloat(),
            (lastMovement + 1000).toFloat()
        ).toDouble()

        var yCount: Double = e.scaledResolution.scaledHeight_double - 5 - deltaY

        if (yCount.isNaN()) {
            yCount = e.scaledResolution.scaledHeight_double
            deltaY = 0.0
        }

        notificationList.removeIf {
            val splits = StringUtil.splitWithMaxLength(it.text, 20)

            var width = 6
            var height = 6
            var maxWidth = 0

            splits.forEach { text ->
                if (regularFont.getWidth(text) > maxWidth) {
                    maxWidth = regularFont.getWidth(text)
                }

                height += regularFont.getHeight(text)
            }

            width += maxWidth + height
            yCount += -height - 5

            var x = e.scaledResolution.scaledWidth - width - 6.0
            var y = yCount

            if (it.xMod == -1.0) {
                it.xMod = width * 2.0
            }

            // Check if easing in
            if (it.addTime + it.duration > System.currentTimeMillis()
            ) {
                it.xMod = width * 2.0 - AnimationUtil.easeOut(0.0, width * 2.0, it.addTime, 1000.0)
            } else {
                // Ease out
                it.yMod = AnimationUtil.easeOut(0.0, height * 2.0, it.addTime + it.duration, 1000.0)
            }

            x += it.xMod
            y += it.yMod

            if (y > e.scaledResolution.scaledHeight_double) {
                // Apply and remove
                deltaY += height + 5
                lastMovement = System.currentTimeMillis()
                return@removeIf true
            }

            // Render the rectangle
            ShapeUtil.drawFilledRoundedRectangle(
                x.toInt(),
                y.toInt(),
                width,
                height,
                3,
                if (it.dark) -0xdededf else -0x1
            )
            ShapeUtil.drawRoundedRectangle(x.toInt(), y.toInt(), width, height, 3, 2.0f, it.color)

            // Bind no texture
            GlStateManager.bindTexture(0)

            // Bind the icon
            Minecraft.getMinecraft().textureManager.bindTexture(it.icon)

            // Check if dark
            val darkColor: Float = if (it.dark) 1.0f else 0.1f

            // Apply
            GL11.glColor4f(darkColor, darkColor, darkColor, 1f)

            // GL options
            GlStateManager.disableTexture2D()
            GlStateManager.enableTexture2D()

            // Draw icon
            Gui.drawModalRectWithCustomSizedTexture(
                x.toInt() + 2, y.toInt() + 2, 0f, 0f,
                height - 4,
                height - 4, (height - 4).toFloat(), (height - 4).toFloat()
            )

            // Set text modification
            var textModify = 1

            // Loop through splits
            splits.forEach { text ->
                // Render
                regularFont.drawCenteredString(
                    text,
                    (x + height / 2 + (width - 8) / 2).toInt(),
                    (y + textModify + regularFont.getHeight(text) / 8f).toInt(),
                    if (it.dark) -0x1 else -0xdededf
                )

                // Modify if needed
                textModify += regularFont.getHeight(text)
            }

            false
        }

        // Reset GL
        GlStateManager.enableTexture2D()
        GL11.glColor4f(1f, 1f, 1f, 1f)

        lastYTotal = yCount
    }

    /**
     * Display a notification
     */
    fun displayNotification(n: Notification) = notificationList.add(n)

}