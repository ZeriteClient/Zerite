package cc.zerite.client.game.gui.settings

import cc.zerite.client.injection.bootstrap.impl.keybinds.CustomKeyBind
import net.minecraft.client.Minecraft
import org.lwjgl.input.Keyboard

class SettingsKeyBind : CustomKeyBind("Zerite Settings", Keyboard.KEY_GRAVE) {

    override fun onPressed() {
        Minecraft.getMinecraft().displayGuiScreen(GuiZeriteSettings)
    }
}