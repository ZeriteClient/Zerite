package net.zeriteclient.zerite.util.other

import net.minecraft.launchwrapper.Launch
import net.zeriteclient.zerite.injection.bootstrap.DiscovererBootstrap
import org.reflections.Reflections
import org.reflections.scanners.FieldAnnotationsScanner
import org.reflections.util.ConfigurationBuilder
import java.lang.reflect.Field
import java.lang.reflect.Method
import kotlin.streams.toList

object ReflectionUtil {

    var reflections: Reflections? = null
        private set
        get() {
            if (field == null) {
                field = Reflections(
                    ConfigurationBuilder().setExpandSuperTypes(false).addScanners(FieldAnnotationsScanner()).addUrls(
                        DiscovererBootstrap.injectClassLoader!!.urLs.asList()
                    ).addUrls(Launch.classLoader.urLs.asList().stream().filter { t ->
                        t.path.toLowerCase().contains("zerite")
                    }.toList()).addClassLoaders(DiscovererBootstrap.injectClassLoader, Launch.classLoader)
                )
            }

            return field
        }

    /**
     * Gets a field based on multiple names
     *
     * @param clazz [Class] The class to search through
     * @param fieldNames [String[]] The field names
     * @return [Field] The returned field
     */
    fun getField(clazz: Class<*>, fieldNames: Array<String>): Field? {
        // Iterate field names
        for (name in fieldNames) {
            try {
                // Get field
                val f = clazz.getDeclaredField(name)

                // Check if not null
                if (f != null) {
                    // Set accessible and return
                    f.isAccessible = true
                    return f
                }
            } catch (ignored: NoSuchFieldException) {
            }

        }

        // Return fallback
        return null
    }

    fun getMethod(clazz: Class<*>, methodNames: Array<String>, parameters: Array<Class<*>>): Method? {
        for (name in methodNames) {
            try {
                val m = clazz.getDeclaredMethod(name, *parameters)
                if (m != null) {
                    m.isAccessible = true
                    return m
                }
            } catch (ignored: NoSuchMethodException) {
            }

        }
        return null
    }

}