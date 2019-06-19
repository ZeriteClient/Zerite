package net.zeriteclient.zerite.injection.mixins.util;

import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ScreenShotHelper;
import net.zeriteclient.zerite.injection.mixinsimp.util.MixinScreenShotHelperImpl;
import net.zeriteclient.zerite.util.game.ChatUtil;
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
        MixinScreenShotHelperImpl.INSTANCE.saveScreenshot(gameDirectory, width, height, buffer);
        return ChatUtil.INSTANCE.createComponent("Saving screenshot...");
    }
}
