package net.zeriteclient.zerite.game.options

import net.zeriteclient.zerite.game.gui.settings.EnumSettingCategory
import net.zeriteclient.zerite.injection.bootstrap.impl.annotations.ConfigSettings
import net.zeriteclient.zerite.injection.bootstrap.impl.annotations.Instance
import net.zeriteclient.zerite.injection.bootstrap.impl.configuration.StoreConfig

@Instance(registerConfig = true)
@ConfigSettings("GUI", EnumSettingCategory.GENERAL)
object GuiOptions {

    @StoreConfig("Blue Gradient")
    var blueGradient = true

}