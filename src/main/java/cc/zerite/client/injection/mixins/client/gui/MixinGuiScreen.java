package cc.zerite.client.injection.mixins.client.gui;

import cc.zerite.client.injection.mixinsimp.client.gui.MixinGuiScreenImpl;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen extends Gui {

    @Shadow public int width;
    @Shadow public int height;

    private MixinGuiScreenImpl impl = new MixinGuiScreenImpl((GuiScreen) (Object) this);

    /**
     * @author Koding
     */
    @Overwrite
    public void drawWorldBackground(int tint) {
        impl.drawWorldBackground(tint, width, height);
    }
}
