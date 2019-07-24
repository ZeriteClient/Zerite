package cc.zerite.client.game.cosmetics.capes

import cc.zerite.client.game.cosmetics.capes.CapeHelper.parseCape
import net.minecraft.client.renderer.IImageBuffer
import net.minecraft.util.ResourceLocation
import java.awt.image.BufferedImage
import java.util.*

class CapeImageBuffer(private val uuid: UUID, private val rl: ResourceLocation) : IImageBuffer {

    override fun parseUserSkin(image: BufferedImage) = parseCape(image)

    override fun skinAvailable() {
        CapeHelper.capes[uuid] = rl
    }
}