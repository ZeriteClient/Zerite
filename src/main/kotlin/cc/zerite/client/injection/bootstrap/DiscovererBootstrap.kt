package cc.zerite.client.injection.bootstrap

import cc.zerite.client.util.other.ReflectionUtil
import cc.zerite.client.util.other.StorageUtil
import net.minecraft.launchwrapper.Launch
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

        bootstrapDir.listFiles().filter { it.isFile && it.name.endsWith(".jar", true) }.forEach {
            logger.info("Discovered bootstrap file: ${it.name}")
            injectUrls.add(it.toURI().toURL())
        }

        injectClassLoader = URLClassLoader(injectUrls.toTypedArray(), classLoader)

        logger.info("Discovering bootstraps")

        ReflectionUtil.reflections!!.getSubTypesOf(AbstractBootstrap::class.java).forEach {
            logger.info("Discovered bootstrap class: ${it.name}")
            bootstraps.add(it.newInstance())
        }

        bootstraps.sortByDescending { it.priority }
        bootstraps.forEach { it.bootstrapTweaker() }
    }

}