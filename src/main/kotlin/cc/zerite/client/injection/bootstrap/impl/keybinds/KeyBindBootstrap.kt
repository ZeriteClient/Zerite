package cc.zerite.client.injection.bootstrap.impl.keybinds

import cc.zerite.client.injection.bootstrap.AbstractBootstrap
import cc.zerite.client.util.other.ReflectionUtil
import net.minecraft.client.Minecraft

class KeyBindBootstrap : AbstractBootstrap() {

    val keyBinds: ArrayList<CustomKeyBind> = arrayListOf()

    override fun bootstrapClientInit() {
        keyBinds.addAll(ReflectionUtil.reflections!!.getSubTypesOf(CustomKeyBind::class.java).map { it.newInstance() as CustomKeyBind })

        val persistence = KeyBindObject.keyCodes

        // Get binds
        keyBinds.filter { persistence.containsKey(it.keyDescription) }.forEach {
            it.keyCode = persistence[it.keyDescription] ?: return@forEach
        }

        // Get current key binds
        val newBinds = ArrayList(Minecraft.getMinecraft().gameSettings.keyBindings.clone().toList())

        // Add all current binds
        newBinds.addAll(keyBinds)

        // Replace
        Minecraft.getMinecraft().gameSettings.keyBindings = newBinds.toTypedArray()
    }

    override fun bootstrapClientShutdown() {
        keyBinds.forEach {
            KeyBindObject.keyCodes.putIfAbsent(it.keyDescription, it.keyCode)
            KeyBindObject.keyCodes.replace(it.keyDescription, it.keyCode)
        }
    }
}