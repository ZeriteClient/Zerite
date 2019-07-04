package net.zeriteclient.zerite.injection.mixins.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @Shadow public Vec3 getLook(float partialTicks) {
        return null;
    }

}
