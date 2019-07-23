package net.zeriteclient.zerite.injection.mixinsimp.util

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.shader.Framebuffer
import net.minecraft.util.ScreenShotHelper
import net.zeriteclient.zerite.game.features.integrations.screenshots.AsyncScreenshotSaver
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import java.io.File
import java.nio.IntBuffer

class MixinScreenShotHelperImpl(
    val impl: ScreenShotHelper
) {

    companion object {
        fun saveScreenshot(gameDirectory: File, width: Int, height: Int, buffer: Framebuffer) {
            var pixelBuffer: IntBuffer? = null
            var pixelValues: IntArray? = null
            var cWidth = width
            var cHeight = height

            val file1 = File(gameDirectory, "screenshots")
            file1.mkdir()

            if (OpenGlHelper.isFramebufferEnabled()) {
                cWidth = buffer.framebufferTextureWidth
                cHeight = buffer.framebufferTextureHeight
            }

            val i = cWidth * cHeight

            if (pixelBuffer == null || pixelBuffer.capacity() < i) {
                pixelBuffer = BufferUtils.createIntBuffer(i)
                pixelValues = IntArray(i)
            }

            GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1)
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1)
            pixelBuffer!!.clear()

            if (OpenGlHelper.isFramebufferEnabled()) {
                GlStateManager.bindTexture(buffer.framebufferTexture)
                GL11.glGetTexImage(
                    GL11.GL_TEXTURE_2D,
                    0,
                    GL12.GL_BGRA,
                    GL12.GL_UNSIGNED_INT_8_8_8_8_REV,
                    pixelBuffer
                )
            } else {
                GL11.glReadPixels(
                    0,
                    0,
                    cWidth,
                    cHeight,
                    GL12.GL_BGRA,
                    GL12.GL_UNSIGNED_INT_8_8_8_8_REV,
                    pixelBuffer
                )
            }

            pixelBuffer.get(pixelValues!!)

            GlobalScope.launch {
                AsyncScreenshotSaver(gameDirectory, cWidth, cHeight, buffer, pixelValues)()
            }
        }
    }

}