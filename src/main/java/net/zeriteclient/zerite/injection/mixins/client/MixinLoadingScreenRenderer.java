package net.zeriteclient.zerite.injection.mixins.client;

import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.zeriteclient.zerite.util.rendering.GuiUtil;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LoadingScreenRenderer.class)
public class MixinLoadingScreenRenderer {

    @Shadow private Minecraft mc;
    @Shadow private long systemTime;
    @Shadow private Framebuffer framebuffer;
    @Shadow private String currentlyDisplayedText;
    @Shadow private String message;

    /**
     * @author Koding
     */
    @Overwrite
    public void setLoadingProgress(int progress) {
        long nanoTime = Minecraft.getSystemTime();

        if (nanoTime - this.systemTime >= 100L) {
            this.systemTime = nanoTime;
            ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            int scaleFactor = scaledresolution.getScaleFactor();
            int scaledWidth = scaledresolution.getScaledWidth();
            int scaledHeight = scaledresolution.getScaledHeight();

            if (OpenGlHelper.isFramebufferEnabled()) {
                this.framebuffer.framebufferClear();
            } else {
                GlStateManager.clear(GL11.GL_ACCUM);
            }

            this.framebuffer.bindFramebuffer(false);
            GlStateManager.matrixMode(GL11.GL_PROJECTION);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -200.0F);

            if (!OpenGlHelper.isFramebufferEnabled()) {
                GlStateManager.clear(16640);
            }

            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            GuiUtil.INSTANCE.drawBackground(scaledWidth, scaledHeight, true);

            if (progress >= 0) {
                int maxLoadingProgress = 100;
                int barTop = 2;
                int barWidth = scaledWidth / 2 - maxLoadingProgress / 2;
                int barHeight = scaledHeight / 2 + 16;
                GlStateManager.disableTexture2D();
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                worldrenderer.pos(barWidth, barHeight, 0.0D).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos(barWidth, barHeight + barTop, 0.0D).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos(barWidth + maxLoadingProgress, barHeight + barTop, 0.0D).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos(barWidth + maxLoadingProgress, barHeight, 0.0D).color(128, 128, 128, 255).endVertex();
                worldrenderer.pos(barWidth, barHeight, 0.0D).color(128, 255, 128, 255).endVertex();
                worldrenderer.pos(barWidth, barHeight + barTop, 0.0D).color(128, 255, 128, 255).endVertex();
                worldrenderer.pos(barWidth + progress, barHeight + barTop, 0.0D).color(128, 255, 128, 255).endVertex();
                worldrenderer.pos(barWidth + progress, barHeight, 0.0D).color(128, 255, 128, 255).endVertex();
                tessellator.draw();
                GlStateManager.enableTexture2D();
            }

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
            this.mc.fontRendererObj.drawStringWithShadow(this.currentlyDisplayedText, (float) ((scaledWidth - this.mc.fontRendererObj.getStringWidth(this.currentlyDisplayedText)) / 2), (float) (scaledHeight / 2 - 4 - 16), 16777215);
            this.mc.fontRendererObj.drawStringWithShadow(this.message, (float) ((scaledWidth - this.mc.fontRendererObj.getStringWidth(this.message)) / 2), (float) (scaledHeight / 2 - 4 + 8), 16777215);
            this.framebuffer.unbindFramebuffer();

            if (OpenGlHelper.isFramebufferEnabled()) {
                this.framebuffer.framebufferRender(scaledWidth * scaleFactor, scaledHeight * scaleFactor);
            }

            this.mc.updateDisplay();

            try {
                Thread.yield();
            } catch (Exception ignored) {

            }
        }
    }
}
