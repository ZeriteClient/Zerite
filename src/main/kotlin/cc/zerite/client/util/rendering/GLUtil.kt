package cc.zerite.client.util.rendering

import org.lwjgl.opengl.GL11

object GLUtil {

    fun preStencil() {
        GL11.glEnable(GL11.GL_STENCIL_TEST)
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT)

        GL11.glColorMask(false, false, false, false)
        GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 255)
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE)
    }

    fun postStencil() {
        GL11.glColorMask(true, true, true, true)
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE)
        GL11.glStencilFunc(GL11.GL_EQUAL, 1, 255)
    }

    fun postStencilDraw() {
        GL11.glDisable(GL11.GL_STENCIL_TEST)
    }

}

fun usingStencil(setup: StencilBuilder.() -> Unit) {
    val stencilBuilder = StencilBuilder()
    stencilBuilder.setup()

    GLUtil.preStencil()
    stencilBuilder.stencil()
    GLUtil.postStencil()
    stencilBuilder.scene()
    GLUtil.postStencilDraw()
}

class StencilBuilder {
    var stencil: () -> Unit = {}
    var scene: () -> Unit = {}
}