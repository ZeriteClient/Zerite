package net.zeriteclient.zerite.injection.mixins.client.entity;

import net.minecraft.client.entity.EntityPlayerSP;
import net.zeriteclient.zerite.injection.mixinsimp.client.entity.MixinEntityPlayerSPImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void sendChatMessage(String message, CallbackInfo ci) {
        MixinEntityPlayerSPImpl.INSTANCE.sendChatMessage(message, ci);
    }

    @Inject(method = "onUpdate", at = @At("RETURN"))
    private void onUpdate(CallbackInfo ci) {
        MixinEntityPlayerSPImpl.INSTANCE.onUpdate(ci);
    }

}
