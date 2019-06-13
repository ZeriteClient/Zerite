package net.zeriteclient.zerite.injection.asm.invocations.client

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.zeriteclient.zerite.event.ClientShutdownEvent
import net.zeriteclient.zerite.event.ClientStartEvent
import net.zeriteclient.zerite.event.EventBus
import net.zeriteclient.zerite.game.gui.SplashRenderer
import net.zeriteclient.zerite.injection.bootstrap.ZeriteBootstrap

object MinecraftInvocations {

    fun handleBeginGameCreate() {
        ZeriteBootstrap.beginGameCreate()
    }

    fun handleClientStartEvent() {
        EventBus.post(ClientStartEvent())
        ZeriteBootstrap.beginClientInit()
    }

    fun handleClientShutdownEvent() {
        EventBus.post(ClientShutdownEvent())
    }

    fun handleDrawSplashScreen() {
        SplashRenderer.drawSplash(Minecraft.getMinecraft().textureManager, false, 255)
    }

    fun handleLoadingSounds() {
        SplashRenderer.updateData("Loading sounds")
    }

    fun handleLoadingFontRenderer() {
        SplashRenderer.updateData("Loading font renderer")
    }

    fun handleLoadingRenderEngine() {
        SplashRenderer.updateData("Loading render engine")
    }

    fun handleLoadingRenderGlobal() {
        SplashRenderer.updateData("Loading render global")
    }

    fun handleLoadFinalizing() {
        SplashRenderer.updateData("Loading render global")
    }

    fun handleDisplayGuiScreen(screen: GuiScreen) {

    }

}