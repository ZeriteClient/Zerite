package net.zeriteclient.zerite.game.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.*
import net.minecraft.util.ResourceLocation
import net.zeriteclient.zerite.game.gui.components.IconButton
import net.zeriteclient.zerite.game.gui.components.LinkButton
import net.zeriteclient.zerite.game.gui.settings.GuiZeriteSettings
import net.zeriteclient.zerite.game.tools.font.ZeriteFonts
import net.zeriteclient.zerite.util.EnumBackground
import net.zeriteclient.zerite.util.other.TimeUtil
import net.zeriteclient.zerite.util.rendering.ShapeUtil
import java.awt.Color

class GuiZeriteMainMenu : GuiScreen() {

    companion object {
        private var overlayOpacity = 255.0
    }

    override fun initGui() {
        val regularFont = ZeriteFonts.regular

        buttonList.add(GuiButton(0, width / 2 - 102, height / 2 + 10, 100, 20, "Singleplayer"))
        buttonList.add(GuiButton(1, width / 2 + 2, height / 2 + 10, 100, 20, "Multiplayer"))

        buttonList.add(LinkButton(2, 2, 2, "Settings"))
        buttonList.add(LinkButton(3, 4 + regularFont.getWidth("Settings"), 2, "Cosmetics"))

        buttonList.add(IconButton(4, width - 22, 2, 20, 20, ResourceLocation("textures/icons/close.png")))
        buttonList.add(IconButton(5, width - 42, 2, 20, 20, ResourceLocation("textures/icons/settings.png")))
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        Minecraft.getMinecraft().textureManager.bindTexture(EnumBackground.BACKGROUND_1.location)
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0f, 0f, width, height, width.toFloat(), height.toFloat())

        ShapeUtil.drawGradientRect(
            0.0,
            0.0,
            width.toDouble(),
            height.toDouble(),
            Color(3, 169, 244, 100).rgb,
            Color(2, 136, 209, 255).rgb
        )

        var s = "ZERITE"
        ZeriteFonts.titleLarge.drawCenteredString(
            s, width / 2,
            height / 2 - 10 - (ZeriteFonts.titleLarge.getHeight(s) / 1.5).toInt(),
            Color(255, 255, 255, 255).rgb
        )

        val mediumFont = ZeriteFonts.medium
        val regularFont = ZeriteFonts.regular

        s = "Copyright Mojang AB"
        regularFont.drawString(s, 2, height - 2 - regularFont.getHeight(s) * 2, Color(255, 255, 255, 255).rgb)

        s = "Developed by the Zerite team"
        mediumFont.drawString(s, 2, height - 2 - mediumFont.getHeight(s), Color(255, 255, 255, 255).rgb)

        s = "Logged in"
        regularFont.drawString(
            s,
            width - 2 - regularFont.getWidth(s),
            height - 2 - regularFont.getHeight(s) * 2,
            Color(255, 255, 255, 255).rgb
        )

        s = Minecraft.getMinecraft().session.profile.name ?: "Developer"
        mediumFont.drawString(
            s,
            width - 2 - mediumFont.getWidth(s),
            height - 2 - mediumFont.getHeight(s),
            Color(255, 255, 255, 255).rgb
        )

        super.drawScreen(mouseX, mouseY, partialTicks)

        overlayOpacity = Math.max(0.0, overlayOpacity - Math.max(30.0, TimeUtil.delta / 20.0))
        println(overlayOpacity)
        SplashRenderer.drawSplash(Minecraft.getMinecraft().textureManager, true, overlayOpacity.toInt())
    }

    override fun actionPerformed(button: GuiButton?) {
        when {
            button!!.id == 0 -> Minecraft.getMinecraft().displayGuiScreen(GuiSelectWorld(this))
            button.id == 1 -> Minecraft.getMinecraft().displayGuiScreen(GuiMultiplayer(this))
            button.id == 2 -> Minecraft.getMinecraft().displayGuiScreen(GuiZeriteSettings())
            button.id == 4 -> Minecraft.getMinecraft().shutdown()
            button.id == 5 -> Minecraft.getMinecraft().displayGuiScreen(GuiOptions(this, Minecraft.getMinecraft().gameSettings))
        }
    }
}