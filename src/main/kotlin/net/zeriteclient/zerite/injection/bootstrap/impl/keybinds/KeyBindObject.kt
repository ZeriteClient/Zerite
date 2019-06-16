package net.zeriteclient.zerite.injection.bootstrap.impl.keybinds

import net.minecraft.client.Minecraft
import net.zeriteclient.zerite.event.Subscribe
import net.zeriteclient.zerite.event.TickEvent
import net.zeriteclient.zerite.injection.bootstrap.ZeriteBootstrap
import net.zeriteclient.zerite.injection.bootstrap.impl.annotations.Instance
import net.zeriteclient.zerite.injection.bootstrap.impl.configuration.StoreConfig
import org.lwjgl.input.Keyboard

@Instance(registerConfig = true, registerEvents = true)
object KeyBindObject {

    @StoreConfig
    var keyCodes: HashMap<String, Int> = hashMapOf()

    private val keyPressStatusMap: HashMap<Int, Boolean> = hashMapOf()

    @Subscribe
    private fun onTick(e: TickEvent) {
        // Return if screen is not null
        if (Minecraft.getMinecraft().currentScreen != null) {
            return
        }

        // Get the key bind bootstrap
        val bootstrap = ZeriteBootstrap.getBootstrap<KeyBindBootstrap>()

        // Loop through binds
        bootstrap.keyBinds.forEach {
            // Get key code and press status
            val key = it.keyCode
            val pressed = Keyboard.isKeyDown(key)

            // Fire held if pressed
            if (pressed) {
                it.onHeld()
            }

            // Put if absent
            keyPressStatusMap.putIfAbsent(key, false)

            // Get boolean
            val keyBool: Boolean = keyPressStatusMap[key] ?: false

            if (!keyBool && pressed) {
                // Fire press event
                it.onPressed()
                keyPressStatusMap.replace(key, true)
            } else if (!pressed && keyBool) {
                // Fire release event
                it.onReleased()
                keyPressStatusMap.replace(key, false)
            }
        }
    }

}