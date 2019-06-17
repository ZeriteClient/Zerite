package net.zeriteclient.zerite.injection.mixinsimp.client

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.multiplayer.WorldClient
import net.zeriteclient.zerite.event.EventBus
import net.zeriteclient.zerite.event.RenderTickEvent
import net.zeriteclient.zerite.event.WorldLoadEvent
import org.lwjgl.opengl.Display
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo


object MixinMinecraftImpl {

    fun runGameLoop(ci: CallbackInfo) {
        EventBus.post(RenderTickEvent(ScaledResolution(Minecraft.getMinecraft())))
    }

    fun loadWorld(worldClient: WorldClient?, message: String, ci: CallbackInfo) {
        EventBus.post(WorldLoadEvent(worldClient ?: Minecraft.getMinecraft().theWorld ?: return, message))
    }

    fun toggleFullscreen(ci: CallbackInfo) {
        Display.setResizable(false)
        Display.setResizable(true)
    }

}