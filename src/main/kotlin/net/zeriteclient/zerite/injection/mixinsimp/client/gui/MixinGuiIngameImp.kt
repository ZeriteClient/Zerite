package net.zeriteclient.zerite.injection.mixinsimp.client.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.zeriteclient.zerite.event.EventBus
import net.zeriteclient.zerite.event.RenderGameOverlayEvent
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

object MixinGuiIngameImp {

    fun renderGameOverlay(partialTicks: Float, ci: CallbackInfo) {
        EventBus.post(RenderGameOverlayEvent(ScaledResolution(Minecraft.getMinecraft()), partialTicks))
    }

}