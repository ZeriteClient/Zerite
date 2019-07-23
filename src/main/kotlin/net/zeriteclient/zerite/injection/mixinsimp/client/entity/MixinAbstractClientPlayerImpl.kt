package net.zeriteclient.zerite.injection.mixinsimp.client.entity

import com.mojang.authlib.GameProfile
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.world.World
import net.zeriteclient.zerite.event.EventBus
import net.zeriteclient.zerite.event.PlayerInitEvent

class MixinAbstractClientPlayerImpl(
    val impl: AbstractClientPlayer
) {

    fun init(worldIn: World, playerProfile: GameProfile) {
        EventBus.post(PlayerInitEvent(impl, worldIn, playerProfile))
    }

}