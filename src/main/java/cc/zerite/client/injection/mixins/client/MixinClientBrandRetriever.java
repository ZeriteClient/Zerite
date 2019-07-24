package cc.zerite.client.injection.mixins.client;

import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientBrandRetriever.class)
public class MixinClientBrandRetriever {

    /**
     * @author Koding
     */
    @Overwrite
    public static String getClientModName() {
        return "Zerite";
    }
}
