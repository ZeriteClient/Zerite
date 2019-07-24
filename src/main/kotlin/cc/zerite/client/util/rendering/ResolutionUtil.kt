package cc.zerite.client.util.rendering

import cc.zerite.client.event.RenderTickEvent
import cc.zerite.client.event.Subscribe
import cc.zerite.client.injection.bootstrap.impl.annotations.Instance
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution

@Instance(registerEvents = true)
object ResolutionUtil {

    var scaledResolution: ScaledResolution = ScaledResolution(Minecraft.getMinecraft())
        private set

    @Subscribe(priority = 100)
    private fun onRenderTick(e: RenderTickEvent) {
        scaledResolution = e.scaledResolution
    }

}