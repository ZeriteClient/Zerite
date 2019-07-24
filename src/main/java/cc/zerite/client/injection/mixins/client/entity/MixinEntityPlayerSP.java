package cc.zerite.client.injection.mixins.client.entity;

import cc.zerite.client.injection.mixinsimp.client.entity.MixinEntityPlayerSPImpl;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {

    private MixinEntityPlayerSPImpl impl = new MixinEntityPlayerSPImpl((EntityPlayerSP) (Object) this);

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void sendChatMessage(String message, CallbackInfo ci) {
        impl.sendChatMessage(message, ci);
    }

    @Inject(method = "onUpdate", at = @At("RETURN"))
    private void onUpdate(CallbackInfo ci) {
        impl.onUpdate();
    }

}
