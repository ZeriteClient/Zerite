package net.zeriteclient.zerite.injection.bootstrap.impl.keybinds

import net.minecraft.client.settings.KeyBinding

open class CustomKeyBind(description: String, keyCode: Int, category: String = "Zerite") :
    KeyBinding(description, keyCode, category) {

    open fun onHeld() {}
    open fun onPressed() {}
    open fun onReleased() {}

}