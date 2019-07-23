package net.zeriteclient.zerite.injection.mixinsimp.client.renderer

import net.minecraft.client.renderer.OpenGlHelper

class MixinOpenGlHelperImpl(
    val impl: OpenGlHelper
) {

    companion object {
        var lastBrightnessX = 0.0f
        var lastBrightnessY = 0.0f
    }

}
