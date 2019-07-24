package cc.zerite.client.injection.mixins.client.renderer;

import cc.zerite.client.injection.mixinsimp.client.renderer.MixinOpenGlHelperImpl;
import net.minecraft.client.renderer.OpenGlHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OpenGlHelper.class)
public class MixinOpenGlHelper {

    @Inject(method = "setLightmapTextureCoords", at = @At("RETURN"))
    private static void setLastValues(int target, float brightnessX, float brightnessY, CallbackInfo ci) {
        MixinOpenGlHelperImpl.Companion.setLastBrightnessX(brightnessX);
        MixinOpenGlHelperImpl.Companion.setLastBrightnessY(brightnessY);
    }
}
