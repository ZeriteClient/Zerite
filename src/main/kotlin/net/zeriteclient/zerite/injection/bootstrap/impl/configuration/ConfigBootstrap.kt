package net.zeriteclient.zerite.injection.bootstrap.impl.configuration

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import net.zeriteclient.zerite.injection.bootstrap.AbstractBootstrap
import net.zeriteclient.zerite.util.other.StorageUtil
import org.apache.commons.io.IOUtils
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader

class ConfigBootstrap : AbstractBootstrap() {

    private val registeredObjects: ArrayList<Any> = arrayListOf()
    private val gson = GsonBuilder().setPrettyPrinting().create()
    private var configFile: File? = StorageUtil.getDataFile("config.json")

    override fun bootstrapClientInit() = load()

    override fun bootstrapClientShutdown() = save()

    fun register(obj: Any) {
        registeredObjects.add(obj)
    }

    private fun save() {
        val saveObject = JsonObject()

        for (o in registeredObjects) {
            val clazz = o.javaClass
            val classObject = JsonObject()

            for (f in clazz.declaredFields) {
                if (f.getAnnotation(StoreConfig::class.java) != null) {
                    f.isAccessible = true
                    classObject.add(f.name, gson.toJsonTree(f.get(o), f.type))
                }
            }

            saveObject.add(clazz.name, classObject)
        }

        if (!configFile!!.exists()) configFile!!.createNewFile()
        IOUtils.write(gson.toJson(saveObject), FileOutputStream(configFile))
    }

    private fun load() {
        if (!configFile!!.exists()) return

        val bufferedReader = BufferedReader(FileReader(configFile))
        val jsonObject = gson.fromJson(bufferedReader, JsonObject::class.java) ?: return

        for (o in registeredObjects) {
            val clazz = o.javaClass

            if (!jsonObject.has(clazz.name)) return

            val classObject = jsonObject.getAsJsonObject(clazz.name)

            for (f in clazz.declaredFields) {
                if (f.isAnnotationPresent(StoreConfig::class.java) && classObject.has(f.name)) {
                    f.isAccessible = true
                    f.set(o, gson.fromJson(classObject.get(f.name), TypeToken.get(f.genericType).type))
                }
            }
        }
    }
}