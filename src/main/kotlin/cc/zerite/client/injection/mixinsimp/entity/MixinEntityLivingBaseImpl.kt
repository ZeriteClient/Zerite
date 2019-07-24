package cc.zerite.client.injection.mixinsimp.entity

import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.Vec3
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

class MixinEntityLivingBaseImpl(private val impl: EntityLivingBase) {

    fun getLook(partialTicks: Float, ci: CallbackInfoReturnable<Vec3>, look: Vec3) {
        val base = impl

        if (base is EntityPlayerSP) {
            ci.returnValue = look
        }
    }
}
