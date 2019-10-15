package cc.zerite.client.injection.mixins.util;

import cc.zerite.client.injection.mixinsimp.util.MixinScreenShotHelperImpl;
import cc.zerite.client.util.game.ChatUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ScreenShotHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.io.File;

@Mixin(ScreenShotHelper.class)
public abstract class MixinScreenShotHelper {

    /**
     * @author Koding
     */
    @Overwrite
    public static IChatComponent saveScreenshot(File gameDirectory, int width, int height, Framebuffer buffer) {
        MixinScreenShotHelperImpl.Companion.saveScreenshot(gameDirectory, width, height, buffer);
        return ChatUtil.INSTANCE.createComponent("Saving screenshot...");
    }
}
