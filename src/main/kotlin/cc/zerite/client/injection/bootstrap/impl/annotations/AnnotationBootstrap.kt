package cc.zerite.client.injection.bootstrap.impl.annotations

import cc.zerite.client.event.EventBus
import cc.zerite.client.game.gui.settings.GuiZeriteSettings
import cc.zerite.client.game.gui.settings.element.impl.SettingsToggle
import cc.zerite.client.game.gui.settings.tab.SettingDropdown
import cc.zerite.client.injection.bootstrap.AbstractBootstrap
import cc.zerite.client.injection.bootstrap.EnumStage
import cc.zerite.client.injection.bootstrap.ZeriteBootstrap
import cc.zerite.client.injection.bootstrap.impl.configuration.ConfigBootstrap
import cc.zerite.client.injection.bootstrap.impl.configuration.StoreConfig
import cc.zerite.client.util.other.ReflectionUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.apache.logging.log4j.LogManager
import org.reflections.Reflections
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

class AnnotationBootstrap : AbstractBootstrap() {

    override fun bootstrapGameCreate() {
        val reflections = ReflectionUtil.reflections

        reflections!!.getTypesAnnotatedWith(Instance::class.java).forEach {
            val field = it.getDeclaredField("INSTANCE")
                ?: Reflections(it).getFieldsAnnotatedWith(FieldMarker::class.java).first { field ->
                    field.getAnnotation(
                        FieldMarker::class.java
                    )?.type == MarkerType.INSTANCE
                } ?: return@forEach
            field.isAccessible = true

            val obj = field.get(null) ?: return
            val annotation = it.getDeclaredAnnotation(Instance::class.java)

            if (annotation.registerEvents) {
                EventBus.register(obj)
            }

            if (annotation.registerConfig) {
                ZeriteBootstrap.getBootstrap<ConfigBootstrap>().register(obj)

                if (it.isAnnotationPresent(ConfigSettings::class.java)) {
                    val settings = it.getDeclaredAnnotation(ConfigSettings::class.java)
                    val group = GuiZeriteSettings.groups[settings.category]

                    if (group != null) {
                        if (group.dropDowns.none { d -> d.name.equals(settings.name, ignoreCase = true) }) {
                            group.dropDowns.add(SettingDropdown(settings.name))
                        }

                        val first = group.dropDowns.first { d -> d.name.equals(settings.name, ignoreCase = true) }

                        it.declaredFields.forEach { f ->
                            if (f.isAnnotationPresent(StoreConfig::class.java)) {
                                f.isAccessible = true

                                val config = f.getDeclaredAnnotation(StoreConfig::class.java)
                                val value = f.get(null)

                                if (!config.hidden && value != null) {
                                    when (f.type) {
                                        Boolean::class.java -> {
                                            first.elements.add(SettingsToggle(config.name, { b ->
                                                f.setBoolean(null, b)
                                            }, value as Boolean))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            field.declaringClass.fields.forEach { f: Field ->
                val declaringClass = f.declaringClass
                f.isAccessible = true

                if (f.isAnnotationPresent(FieldMarker::class.java) && !Modifier.isStatic(f.modifiers)) {
                    val marker: FieldMarker = f.getAnnotation(FieldMarker::class.java)

                    if (marker.type == MarkerType.LOGGER)
                        f.set(obj, LogManager.getLogger(declaringClass.simpleName))
                }
            }

            field.declaringClass.methods.forEach { m: Method ->
                //                val declaringClass = m.declaringClass
//                val static = Modifier.isStatic(m.modifiers)

                m.isAccessible = true

                if (m.isAnnotationPresent(Scheduled::class.java) && m.parameterCount == 0) {
                    val scheduled: Scheduled = m.getAnnotation(Scheduled::class.java)

                    GlobalScope.launch {
                        while (isActive) {
                            if (ZeriteBootstrap.stage != EnumStage.CLIENT_INIT) continue

                            m.invoke(obj)
                            delay(scheduled.interval)
                        }
                    }
                }
            }
        }
    }
}