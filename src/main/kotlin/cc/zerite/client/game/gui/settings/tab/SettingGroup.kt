package cc.zerite.client.game.gui.settings.tab

import cc.zerite.client.util.game.MouseUtil
import cc.zerite.client.util.other.TimeUtil
import cc.zerite.client.util.rendering.RenderDimension
import net.minecraft.util.ResourceLocation
import kotlin.math.max


class SettingGroup(val name: String, val icon: ResourceLocation) {

    val dropDowns: ArrayList<SettingDropdown> = arrayListOf()
    var dimension: RenderDimension = RenderDimension(0, 0, 0, 0)

    private var scrollingUp = false
    private var scrollVelocity = 0.0
    private var scrollProgress = 0
    private var lastYPos = 0

    fun draw() {
        scrollProgress = max(0, (scrollProgress + if (!scrollingUp) scrollVelocity else -scrollVelocity).toInt())
        scrollVelocity = max(0.0, scrollVelocity - TimeUtil.delta / 10.0)

        println(scrollVelocity)

        var yPos = dimension.y - scrollProgress

        for (dropdown in dropDowns) {
            dropdown.dimension = RenderDimension(dimension.width, 0, dimension.x, yPos)
            dropdown.draw()

            yPos += dropdown.dimension.height
        }

        lastYPos = yPos
    }

    fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        dropDowns.forEach { it.mouseClicked(mouseX, mouseY, mouseButton) }
    }

    fun mouseScrolled(wheel: Int) {
        if (MouseUtil.isHovered(dimension.x, dimension.y, dimension.width, dimension.height)) {
            scrollingUp = wheel > 0
            scrollVelocity += if (scrollingUp) wheel / 32.0 else -wheel / 32.0
        }
    }

}