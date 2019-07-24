package cc.zerite.client.injection.stages.tweaker

import cc.zerite.client.injection.bootstrap.DiscovererBootstrap
import net.minecraft.launchwrapper.ITweaker
import net.minecraft.launchwrapper.LaunchClassLoader
import org.spongepowered.asm.launch.MixinBootstrap
import org.spongepowered.asm.mixin.Mixins
import java.io.File

class ZeriteTweaker : ITweaker {

    private val arguments: MutableList<String> = mutableListOf()

    override fun acceptOptions(args: MutableList<String>, gameDir: File?, assetsDir: File?, profile: String?) {
        arguments.addAll(
            args + listOf(
                "--gameDir", gameDir?.absolutePath ?: ".",
                "--assetsDir", assetsDir?.absolutePath ?: ".",
                "--version", profile ?: "Zerite"
            )
        )
    }

    override fun getLaunchTarget(): String = "net.minecraft.client.main.Main"

    override fun injectIntoClassLoader(classLoader: LaunchClassLoader?) {
        classLoader!!.addClassLoaderExclusion("cc.zerite.client.injection.bootstrap.DiscovererBootstrap")
        classLoader.addClassLoaderExclusion("cc.zerite.client.injection.bootstrap.AbstractBootstrap")
        classLoader.addTransformerExclusion("cc.zerite.client.injection.bootstrap.")

        MixinBootstrap.init()
        Mixins.addConfiguration("mixins.zerite.json")

        DiscovererBootstrap.bootstrap()
    }

    override fun getLaunchArguments(): Array<String> = arguments.toTypedArray()
}