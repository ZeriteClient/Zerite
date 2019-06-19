package net.zeriteclient.zerite.game.gui.settings

import net.minecraft.client.Minecraft
import net.zeriteclient.zerite.injection.bootstrap.impl.keybinds.CustomKeyBind
import org.lwjgl.input.Keyboard

class SettingsKeyBind : CustomKeyBind("Zerite Settings", Keyboard.KEY_GRAVE) {

    override fun onPressed() {
        Minecraft.getMinecraft().displayGuiScreen(GuiZeriteSettings())
    }
}