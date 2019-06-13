package net.zeriteclient.zerite.injection.asm.transformers.client

import me.falsehonesty.asmhelper.dsl.At
import me.falsehonesty.asmhelper.dsl.InjectionPoint
import me.falsehonesty.asmhelper.dsl.instructions.JumpCondition
import me.falsehonesty.asmhelper.dsl.instructions.argument
import me.falsehonesty.asmhelper.dsl.instructions.ifClause
import me.falsehonesty.asmhelper.dsl.instructions.invokeKOBjectFunction
import me.falsehonesty.asmhelper.dsl.overwrite
import net.zeriteclient.zerite.injection.mixin.Mixin
import org.objectweb.asm.Opcodes

object MinecraftTransformer {

    fun inject() {
        injectBeginGameCreate()
        injectClientStartEvent()
        injectClientShutdownEvent()
        injectDrawSplashScreen()
        injectLoadingSounds()
        injectLoadingFontRenderer()
        injectLoadingRenderEngine()
        injectLoadFinalizing()
        injectDisplayGuiScreen()
    }

    private fun injectBeginGameCreate() = Mixin.injectInvocation(
        "net/minecraft/client/Minecraft.startGame()V",
        "net/minecraft/client/Minecraft.drawSplashScreen(Lnet/minecraft/client/renderer/texture/TextureManager;)V",
        "net/zeriteclient/zerite/injection/asm/invocations/client/MinecraftInvocations.handleBeginGameCreate()V"
    )

    private fun injectClientStartEvent() = Mixin.injectInvocation(
        "net/minecraft/client/Minecraft.startGame()V",
        "net/minecraft/client/gui/GuiIngame.<init>(Lnet/minecraft/client/Minecraft;)V",
        "net/zeriteclient/zerite/injection/asm/invocations/client/MinecraftInvocations.handleClientStartEvent()V"
    )

    private fun injectClientShutdownEvent() = Mixin.injectInvocation(
        "net/minecraft/client/Minecraft.shutdownMinecraftApplet()V",
        "HEAD",
        "net/zeriteclient/zerite/injection/asm/invocations/client/MinecraftInvocations.handleClientShutdownEvent()V"
    )

    private fun injectDrawSplashScreen() = overwrite {
        className = "net.minecraft.client.Minecraft"
        methodName = "drawSplashScreen"
        methodDesc = "(Lnet/minecraft/client/renderer/texture/TextureManager;)V"

        insnList {
            placeLabel(makeLabel())
            invokeKOBjectFunction(
                "net/zeriteclient/zerite/injection/asm/invocations/client/MinecraftInvocations",
                "handleDrawSplashScreen",
                "()V"
            )
            methodReturn()
        }
    }

    private fun injectLoadingSounds() = Mixin.injectInvocation(
        "net/minecraft/client/Minecraft.startGame()V",
        "net/minecraft/world/chunk/storage/AnvilSaveConverter.<init>(Ljava/io/File;)V",
        "net/zeriteclient/zerite/injection/asm/invocations/client/MinecraftInvocations.handleLoadingSounds()V"
    )

    private fun injectLoadingFontRenderer() = Mixin.injectInvocation(
        "net/minecraft/client/Minecraft.startGame()V",
        "net/minecraft/client/gui/FontRenderer.<init>(Lnet/minecraft/client/settings/GameSettings;Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/client/renderer/texture/TextureManager;Z)V",
        "net/zeriteclient/zerite/injection/asm/invocations/client/MinecraftInvocations.handleLoadingFontRenderer()V"
    )

    private fun injectLoadingRenderEngine() = Mixin.injectInvocation(
        "net/minecraft/client/Minecraft.startGame()V",
        " net/minecraft/client/resources/IReloadableResourceManager.registerReloadListener(Lnet/minecraft/client/resources/IResourceManagerReloadListener;)V",
        "net/zeriteclient/zerite/injection/asm/invocations/client/MinecraftInvocations.handleLoadingRenderGlobal()V"
    )

    private fun injectLoadFinalizing() = Mixin.injectInvocation(
        "net/minecraft/client/Minecraft.startGame()V",
        "net/minecraft/client/particle/EffectRenderer.<init>(Lnet/minecraft/world/World;Lnet/minecraft/client/renderer/texture/TextureManager;)V",
        "net/zeriteclient/zerite/injection/asm/invocations/client/MinecraftInvocations.handleLoadFinalizing()V"
    )

    private fun injectDisplayGuiScreen() = me.falsehonesty.asmhelper.dsl.inject {
        className = "net.minecraft.client.Minecraft"
        methodName = "displayGuiScreen"
        methodDesc = "(Lnet/minecraft/client/gui/GuiScreen;)V"
        at = At(InjectionPoint.CUSTOM {methodNode ->
            listOf(methodNode.instructions.toArray().asList().first {
                it.opcode == Opcodes.INSTANCEOF
            })
        })

        insnList {
            aload(1)
            instanceof("net/minecraft/client/gui/GuiMainMenu")

            ifClause(JumpCondition.EQUAL) {
                invokeKOBjectFunction(
                    "net/zeriteclient/zerite/injection/asm/invocations/client/MinecraftInvocations",
                    "handleDisplayGuiScreen",
                    "(Lnet/minecraft/client/gui/GuiScreen;)V"
                ) {
                    argument {
                        aload(1)
                    }
                }
            }
        }
    }
}