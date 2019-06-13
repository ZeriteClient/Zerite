package net.zeriteclient .zerite.injection.stages.tweaker

import net.minecraft.launchwrapper.ITweaker
import net.minecraft.launchwrapper.LaunchClassLoader
import net.zeriteclient.zerite.injection.bootstrap.DiscovererBootstrap
import java.io.File

class ZeriteTweaker : ITweaker {

    private val arguments: MutableList<String> = mutableListOf()

    override fun acceptOptions(args: MutableList<String>?, gameDir: File?, assetsDir: File?, profile: String?) {
        arguments.addAll(args?: listOf())
        arguments.addAll(listOf(
            "--gameDir", gameDir?.absolutePath ?: ".",
            "--assetsDir", assetsDir?.absolutePath ?: ".",
            "--version", profile ?: "Zerite"
        ))
    }

    override fun getLaunchTarget(): String = "net.minecraft.client.main.Main"

    override fun injectIntoClassLoader(classLoader: LaunchClassLoader?) {
        classLoader!!.addClassLoaderExclusion("net.zeriteclient.zerite.injection.bootstrap.DiscovererBootstrap")
        classLoader.addClassLoaderExclusion("net.zeriteclient.zerite.injection.bootstrap.AbstractBootstrap")
        classLoader.addTransformerExclusion("net.zeriteclient.zerite.injection.bootstrap.")
        classLoader.addTransformerExclusion("net.zeriteclient.zerite.injection.asm.transformers.")
        classLoader.addTransformerExclusion("net.zeriteclient.zerite.injection.asm.ZeriteClassTransformer")
        classLoader.addTransformerExclusion("net.zeriteclient.zerite.injection.mixin.")

        classLoader.registerTransformer("net.zeriteclient.zerite.injection.asm.ZeriteClassTransformer")

        DiscovererBootstrap.bootstrap()
    }

    override fun getLaunchArguments(): Array<String> = arguments.toTypedArray()
}