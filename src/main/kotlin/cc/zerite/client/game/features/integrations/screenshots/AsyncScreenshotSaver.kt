package cc.zerite.client.game.features.integrations.screenshots

import cc.zerite.client.util.game.ChatUtil
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.texture.TextureUtil
import net.minecraft.client.shader.Framebuffer
import net.minecraft.util.EnumChatFormatting
import org.apache.logging.log4j.LogManager
import java.awt.image.BufferedImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.imageio.ImageIO

class AsyncScreenshotSaver(
    private val gameDirectory: File,
    var width: Int,
    var height: Int,
    private var buffer: Framebuffer,
    private var pixelValues: IntArray
) : () -> Unit {

    private val logger = LogManager.getLogger()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")

    override fun invoke() {
        try {
            TextureUtil.processPixelValues(pixelValues, width, height)
            val image: BufferedImage?

            if (OpenGlHelper.isFramebufferEnabled()) {
                image = BufferedImage(buffer.framebufferWidth, buffer.framebufferHeight, 1)
                val j = buffer.framebufferTextureHeight - buffer.framebufferHeight

                for (k in j until buffer.framebufferTextureHeight) {
                    for (l in 0 until buffer.framebufferWidth) {
                        image.setRGB(l, k - j, pixelValues[k * buffer.framebufferTextureWidth + l])
                    }
                }
            } else {
                image = BufferedImage(width, height, 1)
                image.setRGB(0, 0, width, height, pixelValues, 0, width)
            }

            val screenshotFile: File = getTimestampedPNGFileForDirectory(File(gameDirectory, "screenshots"))
            ImageIO.write(image, "png", screenshotFile)

            ChatUtil.printChat("Saved screenshot: ${EnumChatFormatting.BOLD}${screenshotFile.name}${EnumChatFormatting.WHITE}.")
        } catch (exception: Exception) {
            logger.warn("Couldn\'t save screenshot", exception as Throwable)
            ChatUtil.printChat("Screenshot saving ${EnumChatFormatting.RED}${EnumChatFormatting.BOLD}failed${EnumChatFormatting.WHITE}.")
        }
    }

    /**
     * Creates a unique PNG file in the given directory named by a timestamp.  Handles cases where the timestamp alone
     * is not enough to create a uniquely named file, though it still might suffer from an unlikely race condition where
     * the filename was unique when this method was called, but another process or thread created a file at the same
     * path immediately after this method returned.
     */
    private fun getTimestampedPNGFileForDirectory(gameDirectory: File): File {
        val s = dateFormat.format(Date()).toString()
        var i = 1

        while (true) {
            val file1 = File(gameDirectory, s + (if (i == 1) "" else "_$i") + ".png")

            if (!file1.exists()) {
                return file1
            }

            ++i
        }
    }

}