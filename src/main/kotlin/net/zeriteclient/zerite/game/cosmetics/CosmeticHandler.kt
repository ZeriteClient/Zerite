package net.zeriteclient.zerite.game.cosmetics

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.zeriteclient.zerite.event.PlayerInitEvent
import net.zeriteclient.zerite.event.Subscribe
import net.zeriteclient.zerite.game.cosmetics.capes.CapeHelper
import net.zeriteclient.zerite.game.external.api.endpoints.ApiUserEndpoints
import net.zeriteclient.zerite.injection.bootstrap.impl.annotations.Instance
import java.util.*

@Instance(registerEvents = true)
object CosmeticHandler {

    @Subscribe
    private fun onPlayerInit(e: PlayerInitEvent) {
        GlobalScope.launch {
            loadPlayer(e.playerProfile.id)
        }
    }

    private fun loadPlayer(uuid: UUID) {
        val response = ApiUserEndpoints.getUser(uuid) ?: return
        if (!response.success) return

        val url = response.cosmeticMeta?.cape?.url ?: return
        CapeHelper.loadCape(uuid, url)
    }

}