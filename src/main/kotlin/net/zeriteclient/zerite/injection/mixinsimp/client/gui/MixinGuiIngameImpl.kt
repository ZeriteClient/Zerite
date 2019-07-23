package net.zeriteclient.zerite.injection.mixinsimp.client.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiIngame
import net.minecraft.client.gui.ScaledResolution
import net.zeriteclient.zerite.event.EventBus
import net.zeriteclient.zerite.event.RenderGameOverlayEvent

class MixinGuiIngameImpl(
    val impl: GuiIngame
) {
    fun renderGameOverlay(partialTicks: Float) {
        EventBus.post(RenderGameOverlayEvent(ScaledResolution(Minecraft.getMinecraft()), partialTicks))
    }
}