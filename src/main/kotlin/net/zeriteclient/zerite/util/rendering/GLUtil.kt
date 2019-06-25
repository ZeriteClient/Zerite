package net.zeriteclient.zerite.util.rendering

import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11

object GLUtil {

    private val specialStates: HashMap<Int, (to: Boolean) -> Unit> = hashMapOf(
        Pair(GL11.GL_BLEND, { to ->
            if (to) GlStateManager.enableBlend() else GlStateManager.disableBlend()

            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
            GlStateManager.blendFunc(770, 771)
        })
    )
    private val changedStates: HashMap<Int, Boolean> = hashMapOf()

    fun begin(build: StateBuilder.() -> Unit) {
        val builder = StateBuilder()
        builder.build()
        changedStates.putAll(builder.states.map { Pair(it, GL11.glIsEnabled(it)) })
        builder.changedStates.forEach {
            if (it.value) GL11.glEnable(it.key) else GL11.glDisable(it.key)
            specialStates[it.key]?.invoke(it.value)
        }
    }

    fun end() {
        changedStates.forEach { if (it.value) GL11.glEnable(it.key) else GL11.glDisable(it.key) }
        changedStates.clear()
    }

}

class StateBuilder {
    val states: ArrayList<Int> = arrayListOf()
    val changedStates: HashMap<Int, Boolean> = hashMapOf()

    private fun addState(state: Int) = states.add(state)
    fun changeState(state: Int, to: Boolean) {
        addState(state)
        changedStates[state] = to
    }

    operator fun plus(int: Int) = addState(int)
}