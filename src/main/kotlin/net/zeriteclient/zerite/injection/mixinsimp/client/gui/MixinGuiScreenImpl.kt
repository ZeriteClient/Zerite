package net.zeriteclient.zerite.injection.mixinsimp.client.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.zeriteclient.zerite.util.EnumBackground
import net.zeriteclient.zerite.util.rendering.ShapeUtil
import java.awt.Color

object MixinGuiScreenImpl {

    fun drawWorldBackground(tint: Int, width: Int, height: Int) {
        if (Minecraft.getMinecraft().theWorld == null) {
            EnumBackground.BACKGROUND_1.bind()
            Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, width, height, width.toFloat(), height.toFloat())
        }

        ShapeUtil.drawGradientRect(
            0.0,
            0.0,
            width.toDouble(),
            height.toDouble(),
            Color(3, 169, 244, 100).rgb,
            Color(2, 136, 209, 255).rgb
        )
    }

}