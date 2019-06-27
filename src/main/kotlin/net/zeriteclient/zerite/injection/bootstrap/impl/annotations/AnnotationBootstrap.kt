package net.zeriteclient.zerite.injection.bootstrap.impl.annotations

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import net.zeriteclient.zerite.event.EventBus
import net.zeriteclient.zerite.game.gui.settings.GuiZeriteSettings
import net.zeriteclient.zerite.game.gui.settings.element.impl.SettingsToggle
import net.zeriteclient.zerite.game.gui.settings.tab.SettingDropdown
import net.zeriteclient.zerite.injection.bootstrap.AbstractBootstrap
import net.zeriteclient.zerite.injection.bootstrap.EnumStage
import net.zeriteclient.zerite.injection.bootstrap.ZeriteBootstrap
import net.zeriteclient.zerite.injection.bootstrap.impl.configuration.ConfigBootstrap
import net.zeriteclient.zerite.injection.bootstrap.impl.configuration.StoreConfig
import net.zeriteclient.zerite.util.other.ReflectionUtil
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