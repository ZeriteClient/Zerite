package net.zeriteclient.zerite.game.features.mods.misc

import net.zeriteclient.zerite.injection.bootstrap.impl.annotations.ConfigSettings
import net.zeriteclient.zerite.injection.bootstrap.impl.annotations.Instance
import net.zeriteclient.zerite.injection.bootstrap.impl.configuration.StoreConfig

@Instance(registerConfig = true)
@ConfigSettings("Misc")
object FastChatMod {

    @StoreConfig("Fast Chat")
    var fastChat = false

}