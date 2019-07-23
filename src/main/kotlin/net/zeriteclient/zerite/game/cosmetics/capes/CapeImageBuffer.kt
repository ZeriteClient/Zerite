package net.zeriteclient.zerite.game.cosmetics.capes

import net.minecraft.client.renderer.IImageBuffer
import net.minecraft.util.ResourceLocation
import net.zeriteclient.zerite.game.cosmetics.capes.CapeHelper.parseCape
import java.awt.image.BufferedImage
import java.util.*

class CapeImageBuffer(private val uuid: UUID, private val rl: ResourceLocation) : IImageBuffer {

    override fun parseUserSkin(image: BufferedImage) = parseCape(image)

    override fun skinAvailable() {
        CapeHelper.capes[uuid] = rl
    }
}