package cc.zerite.client.injection.mixinsimp.client.entity

import cc.zerite.client.event.EventBus
import cc.zerite.client.event.PlayerInitEvent
import com.mojang.authlib.GameProfile
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.world.World

class MixinAbstractClientPlayerImpl(
    val impl: AbstractClientPlayer
) {

    fun init(worldIn: World, playerProfile: GameProfile) {
        EventBus.post(PlayerInitEvent(impl, worldIn, playerProfile))
    }

}