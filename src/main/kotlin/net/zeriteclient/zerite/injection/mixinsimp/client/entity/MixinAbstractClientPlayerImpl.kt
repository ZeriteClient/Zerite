package net.zeriteclient.zerite.injection.mixinsimp.client.entity

import com.mojang.authlib.GameProfile
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.world.World
import net.zeriteclient.zerite.event.EventBus
import net.zeriteclient.zerite.event.PlayerInitEvent

object MixinAbstractClientPlayerImpl {

    fun init(abstractClientPlayer: AbstractClientPlayer, worldIn: World, playerProfile: GameProfile) {
        EventBus.post(PlayerInitEvent(abstractClientPlayer, worldIn, playerProfile))
    }

}