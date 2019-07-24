package cc.zerite.client.game.tools.notification

import net.minecraft.util.ResourceLocation

class Notification(
    val text: String,
    val duration: Long,
    val icon: ResourceLocation,
    val color: Int = 0x03a9f4,
    val dark: Boolean = true
) {
    val addTime: Long = System.currentTimeMillis()
    var xMod: Double = -1.0
    var yMod: Double = -1.0


}