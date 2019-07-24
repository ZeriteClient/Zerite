package cc.zerite.client.injection.mixinsimp.client

import cc.zerite.client.event.EventBus
import cc.zerite.client.event.RenderTickEvent
import cc.zerite.client.event.WorldLoadEvent
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.multiplayer.WorldClient
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