package net.zeriteclient.zerite.util.other

import net.zeriteclient.zerite.event.RenderTickEvent
import net.zeriteclient.zerite.event.Subscribe
import net.zeriteclient.zerite.injection.bootstrap.impl.annotations.Instance

@Instance(registerEvents = true)
object TimeUtil {

    private var deltaTime: Long = System.currentTimeMillis()
    val delta: Long
        get() = System.currentTimeMillis() - deltaTime

    @Subscribe(priority = -100)
    private fun onRenderTick(e: RenderTickEvent) {
        deltaTime = System.currentTimeMillis()
    }

}