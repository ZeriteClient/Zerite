package net.zeriteclient.zerite.injection.mixinsimp.client

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.multiplayer.WorldClient
import net.zeriteclient.zerite.event.EventBus
import net.zeriteclient.zerite.event.RenderTickEvent
import net.zeriteclient.zerite.event.WorldLoadEvent
import org.lwjgl.opengl.Display


class MixinMinecraftImpl(
    val impl: Minecraft
) {

    fun runGameLoop() {
        EventBus.post(RenderTickEvent(ScaledResolution(impl)))
    }

    fun loadWorld(worldClient: WorldClient?, message: String) {
        EventBus.post(WorldLoadEvent(worldClient ?: impl.theWorld ?: return, message))
    }

    fun toggleFullscreen() {
        Display.setResizable(false)
        Display.setResizable(true)
    }

}