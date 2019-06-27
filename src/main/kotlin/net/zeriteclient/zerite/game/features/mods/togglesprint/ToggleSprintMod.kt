package net.zeriteclient.zerite.game.features.mods.togglesprint

import net.minecraft.client.Minecraft
import net.zeriteclient.zerite.event.RenderGameOverlayEvent
import net.zeriteclient.zerite.event.Subscribe
import net.zeriteclient.zerite.event.TickEvent
import net.zeriteclient.zerite.game.tools.font.ZeriteFonts
import net.zeriteclient.zerite.injection.bootstrap.impl.annotations.ConfigSettings
import net.zeriteclient.zerite.injection.bootstrap.impl.annotations.Instance
import net.zeriteclient.zerite.injection.bootstrap.impl.configuration.StoreConfig
import java.awt.Color

@Instance(registerConfig = true, registerEvents = true)
@ConfigSettings("Toggle Sprint")
object ToggleSprintMod {

    @StoreConfig("Toggled")
    var toggled: Boolean = false

    @Subscribe
    private fun onTick(e: TickEvent) {
        val player = Minecraft.getMinecraft().thePlayer ?: return

        if (player.moveForward > 0 && player.moveStrafing == 0.0f && toggled) {
            player.isSprinting = true
        }
    }

    @Subscribe
    private fun onRenderGameOverlay(e: RenderGameOverlayEvent) {
        if (toggled) {
            ZeriteFonts.mediumSmall.drawString(
                "Sprint Toggled",
                2,
                e.scaledResolution.scaledHeight - ZeriteFonts.mediumSmall.getHeight("Sprint Toggled") - 2,
                Color.WHITE.rgb,
                shadow = false,
                chroma = true
            )
        }
    }

}