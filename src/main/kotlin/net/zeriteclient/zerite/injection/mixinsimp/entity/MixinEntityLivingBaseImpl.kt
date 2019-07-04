package net.zeriteclient.zerite.injection.mixinsimp.entity

import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.Vec3
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

class MixinEntityLivingBaseImpl(private val parent: EntityLivingBase) {

    fun getLook(partialTicks: Float, ci: CallbackInfoReturnable<Vec3>, look: Vec3) {
        val base = parent

        if (base is EntityPlayerSP) {
            ci.returnValue = look
        }
    }
}
