package net.zeriteclient.zerite.injection.mixinsimp.client.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.zeriteclient.zerite.event.EventBus
import net.zeriteclient.zerite.event.RenderGameOverlayEvent

object MixinGuiIngameImp {
    fun renderGameOverlay(partialTicks: Float) {
        EventBus.post(RenderGameOverlayEvent(ScaledResolution(Minecraft.getMinecraft()), partialTicks))
    }
}