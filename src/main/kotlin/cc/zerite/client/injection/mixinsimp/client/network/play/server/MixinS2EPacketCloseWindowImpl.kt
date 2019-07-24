package cc.zerite.client.injection.mixinsimp.client.network.play.server

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraft.network.play.INetHandlerPlayClient
import net.minecraft.network.play.server.S2EPacketCloseWindow
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

class MixinS2EPacketCloseWindowImpl(
    val impl: S2EPacketCloseWindow
) {

    fun processPacket(handler: INetHandlerPlayClient, ci: CallbackInfo) {
        if (Minecraft.getMinecraft().currentScreen is GuiChat) {
            ci.cancel()
        }
    }

}