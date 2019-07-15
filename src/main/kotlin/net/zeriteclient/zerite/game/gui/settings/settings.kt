package net.zeriteclient.zerite.game.gui.settings

import net.minecraft.util.ResourceLocation

enum class EnumSettingCategory(val display: String, val icon: ResourceLocation) {
    GENERAL("General", ResourceLocation("textures/icons/power.png")),
    MODS("Mods", ResourceLocation("textures/icons/settings.png"))
}