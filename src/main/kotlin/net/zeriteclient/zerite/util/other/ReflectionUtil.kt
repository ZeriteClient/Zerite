package net.zeriteclient.zerite.util.other

import net.minecraft.launchwrapper.Launch
import net.zeriteclient.zerite.injection.bootstrap.DiscovererBootstrap
import org.reflections.Reflections
import org.reflections.scanners.FieldAnnotationsScanner
import org.reflections.util.ConfigurationBuilder
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

}