package cc.zerite.client.game.cosmetics

import cc.zerite.client.event.PlayerInitEvent
import cc.zerite.client.event.Subscribe
import cc.zerite.client.event.TickEvent
import cc.zerite.client.event.WorldLoadEvent
import cc.zerite.client.game.cosmetics.capes.CapeHelper
import cc.zerite.client.game.external.api.endpoints.ApiUserEndpoints
import cc.zerite.client.injection.bootstrap.impl.annotations.Instance
import cc.zerite.client.injection.mixins.client.renderer.IMixinThreadDownloadImageData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.minecraft.client.Minecraft
import java.util.*
import cc.zerite.client.injection.mixins.client.renderer.texture.IMixinTextureManager
import net.minecraft.client.renderer.texture.ITextureObject
import net.minecraft.util.ResourceLocation
import java.util.ArrayList
import net.minecraft.client.renderer.IImageBuffer
import net.minecraft.client.renderer.ThreadDownloadImageData
import net.minecraft.client.renderer.texture.TextureManager
import java.lang.reflect.AccessibleObject.setAccessible
import javassist.compiler.MemberResolver.getSuperclass
import net.minecraft.client.model.ModelPlayer
import cc.zerite.client.injection.mixins.client.renderer.entity.IMixinRenderManager
import net.minecraft.client.renderer.entity.RenderPlayer
import net.minecraft.launchwrapper.Launch
import java.lang.reflect.AccessibleObject.setAccessible
import net.minecraft.launchwrapper.LaunchClassLoader
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashMap


@Instance(registerEvents = true)
object CosmeticHandler {

    var locations = arrayListOf<ResourceLocation>()
    private var tickCounter: Int = 0

    @Subscribe
    private fun onPlayerInit(e: PlayerInitEvent) {
        GlobalScope.launch {
            loadPlayer(e.playerProfile.id)
        }
    }

    private fun loadPlayer(uuid: UUID) {
        val response = ApiUserEndpoints.getUser(uuid) ?: return
        if (!response.success) return

        val url = response.cosmeticMeta?.cape?.url ?: return
        CapeHelper.loadCape(uuid, url)
    }

    @Subscribe
    private fun loadWorld(event: WorldLoadEvent) {
        val textureManager = Minecraft.getMinecraft().textureManager
        val mapTextureObjects = (textureManager as IMixinTextureManager).mapTextureObjects
        val removes = arrayListOf<ResourceLocation>()

        for (resourceLocation in mapTextureObjects.keys) {
            val iTextureObject = mapTextureObjects[resourceLocation]
            if (iTextureObject is ThreadDownloadImageData) {
                val imageBuffer = (iTextureObject as IMixinThreadDownloadImageData).imageBuffer
                val aClass = imageBuffer.javaClass

                // Optifine
                if (aClass.name.equals("CapeImageBuffer", ignoreCase = true)) {
                    removes.add(resourceLocation)
                }
            }
        }

        removes.forEach {
            deleteSkin(it)
        }

        locations.forEach {
            deleteSkin(it)
        }

        var del = 0
        for (value in (Minecraft.getMinecraft().renderManager as IMixinRenderManager).skinMap.values) {
            val mainModel = value.mainModel
            val superClass = mainModel.javaClass.superclass
            for (field in superClass.declaredFields) {
                field.isAccessible = true
                try {
                    val o = field.get(mainModel)
                    if (o != null) {
                        try {
                            val entityIn = o.javaClass.superclass.getDeclaredField("entityIn")
                            entityIn.isAccessible = true
                            val o1 = entityIn.get(o)
                            if (o1 != null) {
                                entityIn.set(o, null)
                                del++
                            }
                        } catch (ignored: IllegalAccessException) {
                        } catch (ignored: NoSuchFieldException) {
                        }

                        try {
                            val clientPlayer = o.javaClass.superclass.getDeclaredField("clientPlayer")
                            clientPlayer.isAccessible = true
                            val o1 = clientPlayer.get(o)
                            if (o1 != null) {
                                clientPlayer.set(o, null)
                                del++
                            }
                        } catch (ignored: IllegalAccessException) {
                        } catch (ignored: NoSuchFieldException) {
                        }

                    }
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
        }

        println("Deleted ${removes.size + locations.size + del} cosmetic items / skins")
        locations.clear()
    }

    @Subscribe
    fun tick(event: TickEvent) {
        if (tickCounter % 20 * 60 == 0) {
            Minecraft.memoryReserve = ByteArray(0)
            try {
                val resourceCache = LaunchClassLoader::class.java.getDeclaredField("resourceCache")
                resourceCache.isAccessible = true
                (resourceCache.get(Launch.classLoader) as ConcurrentHashMap<*, *>).clear()

                val packageManifests = LaunchClassLoader::class.java.getDeclaredField("packageManifests")
                packageManifests.isAccessible = true
                (packageManifests.get(Launch.classLoader) as ConcurrentHashMap<*, *>).clear()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            }

        }

        tickCounter++
    }

    fun queueDelete(location: ResourceLocation?) {
        if (location == null) return
        locations.add(location)
    }

    private fun deleteSkin(skinLocation: ResourceLocation?) {
        if (skinLocation == null) return
        val textureManager = Minecraft.getMinecraft().textureManager
        val mapTextureObjects = (textureManager as IMixinTextureManager).mapTextureObjects
        textureManager.deleteTexture(skinLocation)
        mapTextureObjects.remove(skinLocation) // not needed with optifine but needed without it
    }
}