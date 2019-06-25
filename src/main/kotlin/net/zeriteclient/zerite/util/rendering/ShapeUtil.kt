package net.zeriteclient.zerite.util.rendering

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.MathHelper
import org.lwjgl.opengl.GL11

/**
 * Various utilities for rendering shapes into GUIs
 *
 * @author Koding
 * @since 0.1-PRE
 */
object ShapeUtil {

    /**
     * Binds a color integer to OpenGL
     *
     * @param color [Integer] The color
     */
    private fun bindColor(color: Int) {
        // Extract params
        val alpha = (color shr 24 and 255).toFloat() / 255.0f
        val red = (color shr 16 and 255).toFloat() / 255.0f
        val green = (color shr 8 and 255).toFloat() / 255.0f
        val blue = (color and 255).toFloat() / 255.0f

        // Set the color
        GL11.glColor4f(red, green, blue, alpha)
    }

    private fun preDraw() {
        GlStateManager.pushAttrib()
        GlStateManager.pushMatrix()

        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glEnable(GL11.GL_POLYGON_SMOOTH)
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST)
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST)

        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.blendFunc(770, 771)
        GlStateManager.disableTexture2D()
    }

    private fun postDraw() {
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()

        GL11.glDisable(GL11.GL_LINE_SMOOTH)
        GL11.glDisable(GL11.GL_POLYGON_SMOOTH)

        GlStateManager.popAttrib()
        GlStateManager.popMatrix()
        GlStateManager.bindTexture(0)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
    }

    /**
     * Renders a filled circle to the screen
     *
     * @param x [Integer] The x position
     * @param y [Integer] The y position
     * @param radius [Integer] The radius of the circle
     * @param segments [Integer] The amount of segments to divide by
     * @param color [Integer] The color
     */
    fun drawFilledCircle(x: Int, y: Int, radius: Int, segments: Int, color: Int) {
        // GL options
        preDraw()

        // Begin drawing
        GL11.glBegin(GL11.GL_TRIANGLE_FAN)

        // Loop for segments
        for (i in 0 until segments) {
            // Create point positions
            val px = x + radius * MathHelper.sin((i * 0.12566370614359174).toFloat())
            val py = y + radius * MathHelper.cos((i * 0.12566370614359174).toFloat())

            // Bind color
            bindColor(color)

            // Create vertex
            GL11.glVertex2d(px.toDouble(), py.toDouble())
        }

        // End and draw
        GL11.glEnd()

        // GL options
        postDraw()
    }

    /**
     * Draws an arc with a start and an end at the specified position
     *
     * @param cx [Integer] The center x position
     * @param cy [Integer] The center y position
     * @param radius [Integer] The circles radius
     * @param segments [Integer] The amount of segments to draw
     * @param start [Float] The start angle
     * @param angle [Float] The length of the ard
     * @param color [Integer] The color int
     */
    private fun drawArc(
        cx: Int, cy: Int, radius: Int, segments: Int, start: Float, angle: Float,
        color: Int
    ) {
        // Bind the color
        bindColor(color)

        // GL options
        preDraw()

        // Create theta and factors
        val theta = Math.toRadians(angle.toDouble()) / (segments - 1f)
        val tf = Math.tan(theta)

        // Create radial factor
        val rf = Math.cos(theta)

        // Create start position
        var x = radius * Math.cos(Math.toRadians(start.toDouble()))
        var y = radius * Math.sin(Math.toRadians(start.toDouble()))

        // Begin
        GL11.glBegin(GL11.GL_LINE_STRIP)

        // Loop through segments
        for (i in 0 until segments) {
            bindColor(color)

            // Create vertex
            GL11.glVertex2f((x + cx).toFloat(), (y + cy).toFloat())

            // Make changes
            val tx = -y
            val ty = x

            // Add to x
            x += tx * tf
            y += ty * tf

            // Multiply by factor
            x *= rf
            y *= rf
        }

        // End
        GL11.glEnd()

        // GL options
        postDraw()
    }

    /**
     * Draws a filled arc with a start and an end at the specified position
     *
     * @param cx [Integer] The center x position
     * @param cy [Integer] The center y position
     * @param radius [Integer] The circles radius
     * @param segments [Integer] The amount of segments to draw
     * @param start [Float] The start angle
     * @param angle [Float] The length of the ard
     * @param color [Integer] The color int
     */
    private fun drawFilledArc(
        cx: Double, cy: Double, radius: Int, segments: Int, start: Float,
        angle: Float,
        color: Int
    ) {
        // GL options
        preDraw()

        val deg = Math.toRadians(start.toDouble())
        val theta = Math.toRadians(angle.toDouble()) / (segments - 1f)

        bindColor(color)
        GL11.glBegin(GL11.GL_TRIANGLE_FAN)
        GL11.glVertex2d(cx, cy)

        for (i in 0 until segments) {
            val calc = deg - theta * i
            val xInner = radius * Math.cos(calc) + cx
            val yInner = radius * Math.sin(calc) + cy

            GL11.glVertex2d(xInner, yInner)
        }

        GL11.glEnd()

        // GL options
        postDraw()
    }

    /**
     * Draws a filled rectangle with rounded corners
     *
     * @param x [Integer] The x position
     * @param y [Integer] The y position
     * @param width [Integer] The width of this shape
     * @param height [Integer] The height of this shape
     * @param radius [Integer] The radius of the circles in corners
     * @param color [Integer] The color integer
     */
    fun drawFilledRoundedRectangle(
        x: Int, y: Int, width: Int, height: Int, radius: Int,
        color: Int
    ) {
        // GL options
//        preDraw()

        // Bind the color
        bindColor(color)

        // Draw the rectangles for filling
        drawRect(x + radius, y, x + width - radius, y + radius, color)
        drawRect(x + radius, y + height - radius, x + width - radius, y + height, color)
        drawRect(x, y + radius, x + radius, y + height - radius, color)
        drawRect(x + width - radius, y + radius, x + width, y + height - radius, color)
        drawRect(x + radius, y + radius, x + width - radius, y + height - radius, color)

        // Draw the circles in the corners
//        drawArc((x + radius), (y + radius), radius, 50, 270f, 90f, color)
//        drawArc((x + width - radius), (y + radius), radius, 50, 270f, 90f, color)
        drawFilledArc((x + radius).toDouble(), (y + radius).toDouble(), radius, 50, 270f, 90f, color)
        drawFilledArc((x + width - radius).toDouble(), (y + radius).toDouble(), radius, 50, 0f, 90f, color)
        drawFilledArc((x + radius).toDouble(), (y + height - radius).toDouble(), radius, 50, 180f, 90f, color)
        drawFilledArc((x + width - radius).toDouble(), (y + height - radius).toDouble(), radius, 50, 90f, 90f, color)

        // GL options
//        postDraw()
    }

    fun drawRect(left: Int, top: Int, right: Int, bottom: Int, color: Int) {
        var leftVal = left
        var topVal = top
        var rightVal = right
        var bottomVal = bottom

        // GL options
        preDraw()

        if (leftVal < rightVal) {
            val i = leftVal
            leftVal = rightVal
            rightVal = i
        }

        if (topVal < bottomVal) {
            val j = topVal
            topVal = bottomVal
            bottomVal = j
        }

        bindColor(color)

        GL11.glBegin(GL11.GL_TRIANGLE_FAN)

        GL11.glVertex2d(leftVal.toDouble(), bottomVal.toDouble())
        GL11.glVertex2d(rightVal.toDouble(), bottomVal.toDouble())
        GL11.glVertex2d(rightVal.toDouble(), topVal.toDouble())
        GL11.glVertex2d(leftVal.toDouble(), topVal.toDouble())

        GL11.glEnd()

        // GL options
        postDraw()
    }

    /**
     * Draw a rectangle with a width and height
     *
     * @param x [Integer] The x position
     * @param y [Integer] The y position
     * @param width [Integer] The width
     * @param height [Integer] The height
     * @param color [Integer] The color
     */
    fun drawRectWithSize(x: Int, y: Int, width: Int, height: Int, color: Int) {
        drawRect(x, y, x + width, y + height, color)
    }

    /**
     * Draws a line between two points
     *
     * @param sx [Integer] The start x position
     * @param sy [Integer] The start y position
     * @param ex [Integer] The end x position
     * @param ey [Integer] The end y position
     * @param color [Integer] The color int
     */
    fun drawLine(sx: Int, sy: Int, ex: Int, ey: Int, color: Int) {
        // GL options
        preDraw()

        // Bind the color
        bindColor(color)

        // Begin rendering
        GL11.glBegin(GL11.GL_LINE_STRIP)

        // Draw points
        GL11.glVertex2d(sx.toDouble(), sy.toDouble())
        GL11.glVertex2d(ex.toDouble(), ey.toDouble())

        // End
        GL11.glEnd()

        // GL options
        postDraw()
    }

    /**
     * Draws a rounded rectangle with no fill
     *
     * @param x [Integer] The x position
     * @param y [Integer] The y position
     * @param width [Integer] The width of the rectangle
     * @param height [Integer] The height of the rectangle
     * @param radius [Integer] The radius of the corner circles
     * @param lineWidth [Float] The width of the outline
     * @param color [Integer] The color integer
     */
    fun drawRoundedRectangle(
        x: Int, y: Int, width: Int, height: Int, radius: Int,
        lineWidth: Float,
        color: Int
    ) {
        // GL options
        preDraw()

        // Bind the color
        bindColor(color)

        // Set the line width
        GL11.glLineWidth(lineWidth)

        // Draw the corners
        drawArc(x + radius, y + radius, radius, 10, 180f, 90f, color)
        drawArc(x + width - radius, y + radius, radius, 10, 270f, 90f, color)
        drawArc(x + radius, y + height - radius, radius, 10, 90f, 90f, color)
        drawArc(x + width - radius, y + height - radius, radius, 10, 0f, 90f, color)

        // Draw the lines
        drawLine(x + radius, y, x + width - radius, y, color)
        drawLine(x, y + radius, x, y + height - radius, color)
        drawLine(x + width, y + radius, x + width, y + height - radius, color)
        drawLine(x + radius, y + height, x + width - radius, y + height, color)

        // Reset the line width
        GL11.glLineWidth(1f)

        // GL options
        postDraw()
    }

    fun drawGradientRect(x: Double, y: Double, width: Double, height: Double, startColor: Int, endColor: Int) {
        val f = (startColor shr 24 and 255).toFloat() / 255.0f
        val f1 = (startColor shr 16 and 255).toFloat() / 255.0f
        val f2 = (startColor shr 8 and 255).toFloat() / 255.0f
        val f3 = (startColor and 255).toFloat() / 255.0f
        val f4 = (endColor shr 24 and 255).toFloat() / 255.0f
        val f5 = (endColor shr 16 and 255).toFloat() / 255.0f
        val f6 = (endColor shr 8 and 255).toFloat() / 255.0f
        val f7 = (endColor and 255).toFloat() / 255.0f

        GlStateManager.disableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.disableAlpha()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.shadeModel(7425)
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR)
        worldrenderer.pos(x + width, y, 0.0).color(f1, f2, f3, f).endVertex()
        worldrenderer.pos(x, y, 0.0).color((f1 + f5) / 2, (f2 + f6) / 2, (f3 + f6) / 2, (f + f4) / 2).endVertex()
        worldrenderer.pos(x, y + height, 0.0).color(f5, f6, f7, f4).endVertex()
        worldrenderer.pos(x + width, y + height, 0.0).color((f1 + f5) / 2, (f2 + f6) / 2, (f3 + f6) / 2, (f + f4) / 2)
            .endVertex()
        tessellator.draw()
        GlStateManager.shadeModel(7424)
        GlStateManager.disableBlend()
        GlStateManager.enableAlpha()
        GlStateManager.enableTexture2D()
    }

}
