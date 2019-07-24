package cc.zerite.client.injection.bootstrap.impl.annotations

import cc.zerite.client.game.gui.settings.EnumSettingCategory

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Instance(val registerEvents: Boolean = false, val registerConfig: Boolean = false)

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ConfigSettings(val name: String, val category: EnumSettingCategory = EnumSettingCategory.MODS)

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class FieldMarker(val type: MarkerType)

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Scheduled(val interval: Long)

enum class MarkerType {
    INSTANCE,
    LOGGER
}