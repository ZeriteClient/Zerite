package net.zeriteclient.zerite.injection.mixinsimp.client.network

import net.minecraft.client.network.NetHandlerPlayClient
import net.minecraft.network.play.server.S02PacketChat
import net.zeriteclient.zerite.event.EventBus
import net.zeriteclient.zerite.event.ReceiveChatMessageEvent
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