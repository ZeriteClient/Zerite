package cc.zerite.client.injection.mixins.client.gui;

import cc.zerite.client.injection.mixinsimp.client.gui.MixinGuiIngameImpl;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {

    private MixinGuiIngameImpl impl = new MixinGuiIngameImpl((GuiIngame) (Object) this);

    @Inject(method = "renderGameOverlay", at = @At("RETURN"))
    private void renderGameOverlay(float partialTicks, CallbackInfo ci) {
        impl.renderGameOverlay(partialTicks);
    }
}
