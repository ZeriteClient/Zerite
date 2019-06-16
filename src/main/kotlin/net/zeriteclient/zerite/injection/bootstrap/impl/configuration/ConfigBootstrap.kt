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
        configFile!!.createNewFile()

        registeredObjects.add(obj)

        val bufferedReader = BufferedReader(FileReader(configFile))
        val jsonObject = gson.fromJson(bufferedReader, JsonObject::class.java) ?: return

        loadObject(jsonObject, obj)
    }

    private fun save() {
        val saveObject = JsonObject()

        registeredObjects.forEach { o ->
            val clazz = o.javaClass
            val classObject = JsonObject()

            clazz.declaredFields.filter { it.getAnnotation(StoreConfig::class.java) != null }.forEach {
                it.isAccessible = true
                classObject.add(it.name, gson.toJsonTree(it.get(o), it.type))
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

        registeredObjects.forEach {
            loadObject(jsonObject, it)
        }
    }

    private fun loadObject(jsonObject: JsonObject, obj: Any) {
        val clazz = obj.javaClass

        if (!jsonObject.has(clazz.name)) return

        val classObject = jsonObject.getAsJsonObject(clazz.name)

        clazz.declaredFields.filter { it.isAnnotationPresent(StoreConfig::class.java) && classObject.has(it.name) }
            .forEach {
                it.isAccessible = true
                it.set(obj, gson.fromJson(classObject.get(it.name), TypeToken.get(it.genericType).type))
            }
    }
}