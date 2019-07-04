package net.zeriteclient.zerite.injection.mixins.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;
import net.zeriteclient.zerite.injection.mixinsimp.entity.MixinEntityLivingBaseImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLivingBase.class)
public class MixinEntityLivingBase extends MixinEntity {

    private MixinEntityLivingBaseImpl impl = new MixinEntityLivingBaseImpl((EntityLivingBase) (Object) this);

    @Inject(method = "getLook", at = @At("HEAD"), cancellable = true)
    private void getLook(float partialTicks, CallbackInfoReturnable<Vec3> cir) {
        impl.getLook(partialTicks, cir, super.getLook(partialTicks));
    }
}
