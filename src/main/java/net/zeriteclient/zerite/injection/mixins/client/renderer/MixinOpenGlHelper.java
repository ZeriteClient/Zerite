package net.zeriteclient.zerite.injection.mixins.client.renderer;

import net.minecraft.client.renderer.OpenGlHelper;
import net.zeriteclient.zerite.injection.mixinsimp.client.renderer.MixinOpenGlHelperImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OpenGlHelper.class)
public class MixinOpenGlHelper {

    @Inject(method = "setLightmapTextureCoords", at = @At("RETURN"))
    private static void setLastValues(int target, float brightnessX, float brightnessY, CallbackInfo ci) {
        MixinOpenGlHelperImpl.INSTANCE.setLastBrightnessX(brightnessX);
        MixinOpenGlHelperImpl.INSTANCE.setLastBrightnessY(brightnessY);
    }
}
