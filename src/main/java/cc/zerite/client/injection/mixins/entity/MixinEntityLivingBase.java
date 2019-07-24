package cc.zerite.client.injection.mixins.entity;

import cc.zerite.client.injection.mixinsimp.entity.MixinEntityLivingBaseImpl;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;
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
