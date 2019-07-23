package net.zeriteclient.zerite.injection.mixinsimp.client.entity

import net.minecraft.client.entity.EntityPlayerSP
import net.zeriteclient.zerite.event.EventBus
import net.zeriteclient.zerite.event.SendChatMessageEvent
import net.zeriteclient.zerite.event.TickEvent
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

class MixinEntityPlayerSPImpl(
    val impl: EntityPlayerSP
) {

    fun sendChatMessage(message: String, ci: CallbackInfo) {
        // Create event and send
        val event = SendChatMessageEvent(message)
        EventBus.post(event)

        // Check if cancelled
        if (event.cancelled) {
            ci.cancel()
        }
    }

    fun onUpdate() {
        EventBus.post(TickEvent())
    }

}