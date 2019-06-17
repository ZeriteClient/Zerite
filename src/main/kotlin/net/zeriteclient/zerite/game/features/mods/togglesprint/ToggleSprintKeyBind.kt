package net.zeriteclient.zerite.game.features.mods.togglesprint

import com.github.fcannizzaro.material.Colors
import net.minecraft.util.ResourceLocation
import net.zeriteclient.zerite.game.tools.notification.Notification
import net.zeriteclient.zerite.game.tools.notification.NotificationManager
import net.zeriteclient.zerite.injection.bootstrap.impl.keybinds.CustomKeyBind
import org.lwjgl.input.Keyboard

class ToggleSprintKeyBind : CustomKeyBind("Toggle Sprint", Keyboard.KEY_V) {

    override fun onPressed() {
        ToggleSprintMod.toggled = !ToggleSprintMod.toggled
        NotificationManager.displayNotification(
            Notification(
                "Toggle sprint is now ${if (ToggleSprintMod.toggled) "enabled" else "disabled"}.",
                5000,
                ResourceLocation("textures/icons/info.png"),
                color = if (ToggleSprintMod.toggled) Colors.green_400.asColor().rgb else Colors.red_400.asColor().rgb,
                dark = true
            )
        )
    }
}