package cc.zerite.client.injection.mixinsimp.client.gui

import cc.zerite.client.event.EventBus
import cc.zerite.client.event.RenderGameOverlayEvent
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiIngame
import net.minecraft.client.gui.ScaledResolution

class MixinGuiIngameImpl(
    val impl: GuiIngame
) {
    fun renderGameOverlay(partialTicks: Float) {
        EventBus.post(RenderGameOverlayEvent(ScaledResolution(Minecraft.getMinecraft()), partialTicks))
    }
}