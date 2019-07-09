package net.zeriteclient.zerite.game.features.integrations.clearglass

import net.zeriteclient.zerite.game.gui.settings.EnumSettingCategory
import net.zeriteclient.zerite.injection.bootstrap.impl.annotations.ConfigSettings
import net.zeriteclient.zerite.injection.bootstrap.impl.annotations.Instance
import net.zeriteclient.zerite.injection.bootstrap.impl.configuration.StoreConfig

@Instance(registerConfig = true)
@ConfigSettings(name = "Clear Glass", category = EnumSettingCategory.MODS)
object ClearGlass {
    @StoreConfig("Clear Glass")
    var clearGlass = false
}