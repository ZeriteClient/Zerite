package cc.zerite.client.game.cosmetics.capes

import com.mojang.authlib.minecraft.MinecraftProfileTexture
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ThreadDownloadImageData
import net.minecraft.util.ResourceLocation
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import java.util.concurrent.locks.ReentrantLock


object CapeHelper {

    val capes = hashMapOf<UUID, ResourceLocation>()
    private val lock = ReentrantLock()

    fun loadCape(uuid: UUID, url: String) {
        val mpt = MinecraftProfileTexture(url, hashMapOf())
        val rl = ResourceLocation("skins/${mpt.hash}")
        val iib = CapeImageBuffer(uuid, rl)
        val textureCape = ThreadDownloadImageData(
            File(
                Minecraft.getMinecraft().mcDataDir,
                "assets${File.separator}skins${File.separator}${mpt.hash}-xx.png"
            ), mpt.url, null, iib
        )
        lock.lock()
        Minecraft.getMinecraft().textureManager.loadTexture(rl, textureCape)
        lock.unlock()
    }

    fun parseCape(img: BufferedImage): BufferedImage {
        var imageWidth = 64
        var imageHeight = 32

        val srcWidth = img.width
        val srcHeight = img.height

        while (imageWidth < srcWidth || imageHeight < srcHeight) {
            imageWidth *= 2
            imageHeight *= 2
        }

        val imgNew = BufferedImage(imageWidth, imageHeight, 2)
        val g = imgNew.graphics

        g.drawImage(img, 0, 0, null)
        g.dispose()

        return imgNew
    }


}