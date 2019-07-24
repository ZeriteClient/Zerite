package cc.zerite.client.injection.mixins.client.settings;

import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBinding.class)
public interface IMixinKeyBinding {

    @Accessor
    void setPressed(boolean pressed);

}
