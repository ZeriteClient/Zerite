package cc.zerite.client.injection.mixinsimp.client.gui

import cc.zerite.client.util.rendering.GuiUtil
import net.minecraft.client.gui.GuiScreen

class MixinGuiScreenImpl(
    val impl: GuiScreen
) {

    fun drawWorldBackground(tint: Int, width: Int, height: Int) {
        GuiUtil.drawBackground(width, height, tint == Int.MAX_VALUE)
    }
}