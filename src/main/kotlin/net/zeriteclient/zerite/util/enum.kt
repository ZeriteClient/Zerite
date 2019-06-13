package net.zeriteclient.zerite.util

import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation


enum class EnumBackground(val location: ResourceLocation, val width: Int, val height: Int) {

    BACKGROUND_1(ResourceLocation("textures/backgrounds/background-1.png"), 1920, 1018);

    fun bind() {
        Minecraft.getMinecraft().textureManager.bindTexture(location)
    }
}
