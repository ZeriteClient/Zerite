package cc.zerite.client.injection.mixinsimp.client.network

import cc.zerite.client.event.EventBus
import cc.zerite.client.event.ReceiveChatMessageEvent
import net.minecraft.client.network.NetHandlerPlayClient
import net.minecraft.network.play.server.S02PacketChat
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

class MixinNetHandlerPlayClientImpl(
    val impl: NetHandlerPlayClient
) {

    fun handleChat(packetIn: S02PacketChat, ci: CallbackInfo) {
        val event = ReceiveChatMessageEvent(packetIn.chatComponent)
        EventBus.post(event)
        if (event.cancelled) ci.cancel()
    }

}