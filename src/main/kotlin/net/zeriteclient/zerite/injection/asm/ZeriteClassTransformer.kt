package net.zeriteclient.zerite.injection.asm

import me.falsehonesty.asmhelper.BaseClassTransformer
import net.minecraft.launchwrapper.LaunchClassLoader
import net.zeriteclient.zerite.injection.asm.transformers.client.MinecraftTransformer

class ZeriteClassTransformer : BaseClassTransformer() {

    override fun makeTransformers() {
        MinecraftTransformer.inject()
    }
}