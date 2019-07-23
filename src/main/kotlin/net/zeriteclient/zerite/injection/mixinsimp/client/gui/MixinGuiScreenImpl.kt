package net.zeriteclient.zerite.injection.mixinsimp.client.gui

import net.minecraft.client.gui.GuiScreen
import net.zeriteclient.zerite.util.rendering.GuiUtil

class MixinGuiScreenImpl(
    val impl: GuiScreen
) {

    fun drawWorldBackground(tint: Int, width: Int, height: Int) {
        GuiUtil.drawBackground(width, height, tint == Int.MAX_VALUE)
    }
}