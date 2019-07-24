package cc.zerite.client.injection.mixinsimp.client.entity

import cc.zerite.client.event.EventBus
import cc.zerite.client.event.SendChatMessageEvent
import cc.zerite.client.event.TickEvent
import net.minecraft.client.entity.EntityPlayerSP
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