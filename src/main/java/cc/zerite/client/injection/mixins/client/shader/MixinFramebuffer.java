package cc.zerite.client.injection.mixins.client.shader;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.nio.ByteBuffer;

@Mixin(Framebuffer.class)
public abstract class MixinFramebuffer {

    @Shadow public int framebufferWidth;
    @Shadow public int framebufferHeight;
    @Shadow public int framebufferTextureWidth;
    @Shadow public int framebufferTextureHeight;
    @Shadow public abstract void framebufferClear();
    @Shadow public int framebufferObject;
    @Shadow public int framebufferTexture;
    @Shadow public boolean useDepth;
    @Shadow public int depthBuffer;
    @Shadow public abstract void setFramebufferFilter(int filterId);
    @Shadow public abstract void unbindFramebufferTexture();

    /**
     * @author Koding
     * @reason Added support for stencils
     */
    @Overwrite
    public void createFramebuffer(int width, int height) {
        this.framebufferWidth = width;
        this.framebufferHeight = height;
        this.framebufferTextureWidth = width;
        this.framebufferTextureHeight = height;
        if (!OpenGlHelper.isFramebufferEnabled()) {
            this.framebufferClear();
        } else {
            this.framebufferObject = OpenGlHelper.glGenFramebuffers();
            this.framebufferTexture = TextureUtil.glGenTextures();
            if (this.useDepth) {
                this.depthBuffer = OpenGlHelper.glGenRenderbuffers();
            }
            this.setFramebufferFilter(GL11.GL_NEAREST);
            GlStateManager.bindTexture(this.framebufferTexture);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, this.framebufferTextureWidth, this.framebufferTextureHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
            OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, this.framebufferObject);
            OpenGlHelper.glFramebufferTexture2D(OpenGlHelper.GL_FRAMEBUFFER, OpenGlHelper.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, this.framebufferTexture, 0);
            if (this.useDepth) {
                OpenGlHelper.glBindRenderbuffer(OpenGlHelper.GL_RENDERBUFFER, this.depthBuffer);
                OpenGlHelper.glRenderbufferStorage(OpenGlHelper.GL_RENDERBUFFER, GL30.GL_DEPTH24_STENCIL8, this.framebufferTextureWidth, this.framebufferTextureHeight);
                OpenGlHelper.glFramebufferRenderbuffer(OpenGlHelper.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, OpenGlHelper.GL_RENDERBUFFER, this.depthBuffer);
                OpenGlHelper.glFramebufferRenderbuffer(OpenGlHelper.GL_FRAMEBUFFER, GL30.GL_STENCIL_ATTACHMENT, OpenGlHelper.GL_RENDERBUFFER, this.depthBuffer);
            }
            this.framebufferClear();
            this.unbindFramebufferTexture();
        }
    }
}
