package cc.zerite.client.game.features.integrations.clearglass

import cc.zerite.client.game.gui.settings.EnumSettingCategory
import cc.zerite.client.injection.bootstrap.impl.annotations.ConfigSettings
import cc.zerite.client.injection.bootstrap.impl.annotations.Instance
import cc.zerite.client.injection.bootstrap.impl.configuration.StoreConfig

@Instance(registerConfig = true)
@ConfigSettings(name = "Clear Glass", category = EnumSettingCategory.MODS)
object ClearGlass {

    @StoreConfig("Clear Glass")
    var clearGlass = false

}