package cc.zerite.client.game.features.mods.togglesprint

import cc.zerite.client.game.tools.notification.Notification
import cc.zerite.client.game.tools.notification.NotificationManager
import cc.zerite.client.injection.bootstrap.impl.keybinds.CustomKeyBind
import com.github.fcannizzaro.material.Colors
import net.minecraft.util.ResourceLocation
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