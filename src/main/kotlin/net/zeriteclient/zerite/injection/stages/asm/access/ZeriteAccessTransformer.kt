package net.zeriteclient.zerite.injection.stages.asm.access

import com.google.common.collect.Maps
import com.google.common.io.ByteSource
import java.io.IOException
import java.io.InputStream
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile

class ZeriteAccessTransformer @Throws(Exception::class)
constructor() : AccessTransformer("/META-INF/Zerite_at.cfg") {
    init {
        //We are in the new ClassLoader here, so we need to get the static field from the other ClassLoader.
        val classLoader =
            this.javaClass.classLoader.javaClass.classLoader //Bit odd but it gets the class loader that loaded our current class loader yay java!
        val otherClazz = Class.forName(this.javaClass.name, true, classLoader)
        val otherField = otherClazz.getDeclaredField("embedded")
        otherField.isAccessible = true
        embedded = otherField.get(null) as HashMap<String, String>

        for (e in embedded!!.entries) {
            val oldCount = modifiers.size()
            processATFile(e.value)
            val added = modifiers.size() - oldCount
            if (added > 0) {
                logger.info(String.format("Loaded %d rules from AccessTransformer mod jar file %s\n", added, e.key))
            }
        }
    }

    private class JarByteSource(private val jar: JarFile, private val entry: JarEntry) : ByteSource() {
        @Throws(IOException::class)
        override fun openStream(): InputStream = jar.getInputStream(entry)
    }

    companion object {
        private var embedded =
            Maps.newHashMap<String, String>() //Needs to be primitive so that both classloaders get the same class.

        @Throws(IOException::class)
        fun addJar(jar: JarFile) {
            val manifest = jar.manifest
            val atList = manifest.mainAttributes.getValue("AT") ?: return
            for (at in atList.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                val jarEntry = jar.getJarEntry("META-INF/$at")
                if (jarEntry != null) {
                    embedded[String.format("%s!META-INF/%s", jar.name, at)] =
                        JarByteSource(jar, jarEntry).asCharSource(Charsets.UTF_8).read()
                }
            }
        }
    }
}