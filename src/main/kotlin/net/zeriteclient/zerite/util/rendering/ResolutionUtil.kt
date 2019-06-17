package net.zeriteclient.zerite.util.rendering

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.zeriteclient.zerite.event.RenderTickEvent
import net.zeriteclient.zerite.event.Subscribe
import net.zeriteclient.zerite.injection.bootstrap.impl.annotations.Instance

@Instance(registerEvents = true)
object ResolutionUtil {

    var scaledResolution: ScaledResolution = ScaledResolution(Minecraft.getMinecraft())
        private set

    @Subscribe(priority = 100)
    private fun onRenderTick(e: RenderTickEvent) {
        scaledResolution = e.scaledResolution
    }

}