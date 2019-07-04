package net.zeriteclient.zerite.injection.mixins.client.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.zeriteclient.zerite.injection.mixinsimp.client.gui.MixinGuiScreenImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen extends Gui {

    @Shadow public int width;
    @Shadow public int height;

    /**
     * @author Koding
     */
    @Overwrite
    public void drawWorldBackground(int tint) {
        MixinGuiScreenImpl.INSTANCE.drawWorldBackground(tint, width, height);
    }
}
