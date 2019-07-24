package cc.zerite.client.injection.mixins.client.entity;

import cc.zerite.client.game.cosmetics.capes.CapeHelper;
import cc.zerite.client.injection.mixinsimp.client.entity.MixinAbstractClientPlayerImpl;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer {

    @Shadow
    protected abstract NetworkPlayerInfo getPlayerInfo();

    private MixinAbstractClientPlayerImpl impl = new MixinAbstractClientPlayerImpl((AbstractClientPlayer) (Object) this);

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(World worldIn, GameProfile playerProfile, CallbackInfo ci) {
        impl.init(worldIn, playerProfile);
    }

    /**
     * @author Koding
     */
    @Overwrite
    public ResourceLocation getLocationCape() {
        NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        return networkplayerinfo == null ? null : CapeHelper.INSTANCE.getCapes().getOrDefault(networkplayerinfo.getGameProfile().getId(), networkplayerinfo.getLocationCape());
    }
}
