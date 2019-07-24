package cc.zerite.client.game.features.mods.misc

import cc.zerite.client.injection.bootstrap.impl.annotations.ConfigSettings
import cc.zerite.client.injection.bootstrap.impl.annotations.Instance
import cc.zerite.client.injection.bootstrap.impl.configuration.StoreConfig

@Instance(registerConfig = true)
@ConfigSettings("Misc")
object FastChatMod {

    @StoreConfig("Fast Chat")
    var fastChat = false

}