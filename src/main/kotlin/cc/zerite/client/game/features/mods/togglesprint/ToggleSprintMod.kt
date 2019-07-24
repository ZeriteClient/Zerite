package cc.zerite.client.game.features.mods.togglesprint

import cc.zerite.client.event.RenderGameOverlayEvent
import cc.zerite.client.event.Subscribe
import cc.zerite.client.event.TickEvent
import cc.zerite.client.game.tools.font.ZeriteFonts
import cc.zerite.client.injection.bootstrap.impl.annotations.ConfigSettings
import cc.zerite.client.injection.bootstrap.impl.annotations.Instance
import cc.zerite.client.injection.bootstrap.impl.configuration.StoreConfig
import net.minecraft.client.Minecraft
import java.awt.Color

@Instance(registerConfig = true, registerEvents = true)
@ConfigSettings("Toggle Sprint")
object ToggleSprintMod {

    @StoreConfig("Toggled")
    var toggled: Boolean = false

    private var prev = toggled

    @Subscribe
    private fun onTick(e: TickEvent) {
        val sprintKeyBind = Minecraft.getMinecraft().gameSettings.keyBindSprint as cc.zerite.client.injection.mixins.client.settings.IMixinKeyBinding

        if (prev != toggled) {
            prev = toggled
            sprintKeyBind.setPressed(false)
        }

        if (toggled)
            sprintKeyBind.setPressed(true)
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