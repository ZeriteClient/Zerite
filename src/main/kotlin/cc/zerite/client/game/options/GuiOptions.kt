package cc.zerite.client.game.options

import cc.zerite.client.game.gui.settings.EnumSettingCategory
import cc.zerite.client.injection.bootstrap.impl.annotations.ConfigSettings
import cc.zerite.client.injection.bootstrap.impl.annotations.Instance
import cc.zerite.client.injection.bootstrap.impl.configuration.StoreConfig

@Instance(registerConfig = true)
@ConfigSettings("GUI", EnumSettingCategory.GENERAL)
object GuiOptions {

    @StoreConfig("Blue Gradient")
    var blueGradient = true

}