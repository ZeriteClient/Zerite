package net.zeriteclient.zerite.injection.mixins.client.gui;

import net.minecraft.client.gui.GuiIngame;
import net.zeriteclient.zerite.injection.mixinsimp.client.gui.MixinGuiIngameImp;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {

    @Inject(method = "renderGameOverlay", at = @At("RETURN"))
    private void renderGameOverlay(float partialTicks, CallbackInfo ci) {
        MixinGuiIngameImp.INSTANCE.renderGameOverlay(partialTicks, ci);
    }

}
