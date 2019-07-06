package net.zeriteclient.zerite.game.features.mods.blur

import com.google.common.base.Throwables.propagate
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.shader.Shader
import net.minecraft.client.shader.ShaderGroup
import net.minecraft.util.ResourceLocation
import net.zeriteclient.zerite.event.GuiDisplayEvent
import net.zeriteclient.zerite.event.RenderTickEvent
import net.zeriteclient.zerite.event.Subscribe
import net.zeriteclient.zerite.injection.bootstrap.impl.annotations.ConfigSettings
import net.zeriteclient.zerite.injection.bootstrap.impl.annotations.Instance
import net.zeriteclient.zerite.injection.bootstrap.impl.configuration.StoreConfig
import net.zeriteclient.zerite.util.other.ReflectionUtil
import net.zeriteclient.zerite.util.rendering.ShaderUtil
import kotlin.math.min

@Instance(registerEvents = true, registerConfig = true)
@ConfigSettings("Other")
object BlurMod {

    @StoreConfig("Blur GUI backgrounds")
    private var toggled = true

    private var start: Long = 0

    /**
     * Renders the blur shaders to the background of the container
     *
     * @param e the event being used
     */
    @Subscribe
    fun onRenderTick(e: RenderTickEvent) {
        val sg = Minecraft.getMinecraft().entityRenderer.shaderGroup
        try {
            if (!Minecraft.getMinecraft().entityRenderer.isShaderActive || !toggled) {
                return
            }
            val field = ReflectionUtil
                .getField(ShaderGroup::class.java, arrayOf("d", "listShaders"))!!
            field.isAccessible = true
            val shaderList = field.get(sg) as List<Shader>
            for (s in shaderList) {
                val su = s.shaderManager.getShaderUniform("Progress")
                if (su != null) {
                    val fadeTime = 350
                    su.set(min((System.currentTimeMillis() - start) / fadeTime.toFloat(), 1f))
                }
            }
        } catch (ex: IllegalArgumentException) {
            propagate(ex)
        }
    }

    /**
     * Called when a Gui is opened If the opened Gui is GuiChat, cancel the shader
     *
     * @param e the event being used
     */
    @Subscribe
    fun onGuiDisplayed(e: GuiDisplayEvent) {
        if (e.guiScreen == null || e.guiScreen is GuiChat) {
            ShaderUtil.unloadShader()
        } else if (Minecraft.getMinecraft().theWorld != null && Minecraft
                .getMinecraft().theWorld.playerEntities.contains(Minecraft.getMinecraft().thePlayer)
            && !Minecraft.getMinecraft().entityRenderer.isShaderActive
        ) {
            if (!toggled) return

            start = System.currentTimeMillis()
            ShaderUtil
                .applyShader(ResourceLocation("minecraft", "shaders/post/fade_in_blur.json"))
        }
    }

}