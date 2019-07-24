package cc.zerite.client.game.cosmetics

import cc.zerite.client.event.PlayerInitEvent
import cc.zerite.client.event.Subscribe
import cc.zerite.client.game.cosmetics.capes.CapeHelper
import cc.zerite.client.game.external.api.endpoints.ApiUserEndpoints
import cc.zerite.client.injection.bootstrap.impl.annotations.Instance
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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