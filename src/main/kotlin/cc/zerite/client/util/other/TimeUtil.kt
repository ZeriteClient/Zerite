package cc.zerite.client.util.other

import cc.zerite.client.event.RenderTickEvent
import cc.zerite.client.event.Subscribe
import cc.zerite.client.injection.bootstrap.impl.annotations.Instance

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