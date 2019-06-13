package net.zeriteclient.zerite.injection.bootstrap

import net.minecraft.launchwrapper.Launch
import net.zeriteclient.zerite.util.other.ReflectionUtil
import net.zeriteclient.zerite.util.other.StorageUtil
import org.apache.logging.log4j.LogManager
import java.net.URL
import java.net.URLClassLoader

object DiscovererBootstrap {
    val bootstraps: ArrayList<AbstractBootstrap> = arrayListOf()
    var injectClassLoader: URLClassLoader? = null
        private set

    fun bootstrap() {
        val logger = LogManager.getLogger("ZeriteBootstrap")
        val classLoader = Launch.classLoader

        val bootstrapDir = StorageUtil.getDataFile("mods")
        bootstrapDir.mkdirs()

        val injectUrls = mutableListOf<URL>()

        for (file in bootstrapDir.listFiles()) {
            if (!file.isFile || !file.name.toLowerCase().endsWith(".jar")) continue

            logger.info("Discovered bootstrap file: ${file.name}")
            injectUrls.add(file.toURI().toURL())
        }

        injectClassLoader = URLClassLoader(injectUrls.toTypedArray(), classLoader)

        logger.info("Discovering bootstraps")

        for (clazz in ReflectionUtil.reflections!!.getSubTypesOf(AbstractBootstrap::class.java)) {
            logger.info("Discovered bootstrap class: ${clazz.name}")
            bootstraps.add(clazz.newInstance())
        }

        bootstraps.forEach(AbstractBootstrap::bootstrapTweaker)
    }

}