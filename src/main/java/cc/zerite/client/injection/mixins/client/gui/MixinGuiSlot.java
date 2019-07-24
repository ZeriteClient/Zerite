package cc.zerite.client.injection.mixins.client.gui;

import cc.zerite.client.util.rendering.GuiUtil;
import cc.zerite.client.util.rendering.ShapeUtil;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;

@Mixin(GuiSlot.class)
public abstract class MixinGuiSlot {

    @Shadow protected boolean field_178041_q; // visible
    @Shadow protected int mouseX;
    @Shadow protected int mouseY;
    @Shadow protected abstract int getScrollBarX();
    @Shadow protected abstract void bindAmountScrolled();
    @Shadow protected int left;
    @Shadow protected int bottom;
    @Shadow protected float amountScrolled;
    @Shadow protected int top;
    @Shadow protected int width;
    @Shadow public abstract int getListWidth();
    @Shadow protected boolean hasListHeader;
    @Shadow protected abstract void drawListHeader(int p_148129_1_, int p_148129_2_, Tessellator p_148129_3_); // insideLeft insideTop tessellator
    @Shadow protected int height;
    @Shadow public abstract int func_148135_f(); // getMaxScroll
    @Shadow protected abstract int getContentHeight();
    @Shadow protected abstract void func_148142_b(int p_148142_1_, int p_148142_2_); // renderDecorations (mouseXIn, mouseYIn)
    @Shadow protected abstract int getSize();
    @Shadow @Final protected int slotHeight;
    @Shadow protected int headerPadding;
    @Shadow protected abstract void func_178040_a(int p_178040_1_, int p_178040_2_, int p_178040_3_); // updateItemPos (entryID, insideLeft, yPos)
    @Shadow protected boolean showSelectionBox;
    @Shadow protected abstract boolean isSelected(int slotIndex);
    @Shadow protected abstract void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn); // insideLeft yPos insideSlotHeight

    /**
     * @author Koding
     */
    @Overwrite
    public void drawScreen(int mouseXIn, int mouseYIn, float p_148128_3_) {
        if (this.field_178041_q) {
            this.mouseX = mouseXIn;
            this.mouseY = mouseYIn;

            GuiUtil.INSTANCE.drawBackground(width, height, false);

            int i = this.getScrollBarX();
            int j = i + 6;
            this.bindAmountScrolled();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            int k = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            int l = this.top + 4 - (int) this.amountScrolled;

            if (this.hasListHeader) {
                this.drawListHeader(k, l, tessellator);
            }

            GL11.glEnable(GL11.GL_STENCIL_TEST);
            GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);

            GL11.glColorMask(false, false, false, false);
            GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 255);
            GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);

            ShapeUtil.INSTANCE.drawRectWithSize(0, this.top, this.width, this.bottom - this.top, 0xFFFFFFFF);

            GL11.glColorMask(true, true, true, true);
            GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
            GL11.glStencilFunc(GL11.GL_EQUAL, 1, 255);

            this.drawSelectionBox(k, l, mouseXIn, mouseYIn);

            GL11.glDisable(GL11.GL_STENCIL_TEST);

            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();
            int j1 = this.func_148135_f();

            if (j1 > 0) {
                int k1 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                k1 = MathHelper.clamp_int(k1, 32, this.bottom - this.top - 8);
                int l1 = (int) this.amountScrolled * (this.bottom - this.top - k1) / j1 + this.top;

                if (l1 < this.top) {
                    l1 = this.top;
                }

                worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                worldrenderer.pos(i, l1 + k1, 0.0D).color(255, 255, 255, 100).endVertex();
                worldrenderer.pos(j, l1 + k1, 0.0D).color(255, 255, 255, 100).endVertex();
                worldrenderer.pos(j, l1, 0.0D).color(255, 255, 255, 100).endVertex();
                worldrenderer.pos(i, l1, 0.0D).color(255, 255, 255, 100).endVertex();
                tessellator.draw();
            }

            this.func_148142_b(mouseXIn, mouseYIn);
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }

    /**
     * @author Koding
     */
    @Overwrite
    protected void drawSelectionBox(int p_148120_1_, int p_148120_2_, int mouseXIn, int mouseYIn) {
        int i = this.getSize();

        for (int j = 0; j < i; ++j) {
            int k = p_148120_2_ + j * this.slotHeight + this.headerPadding;
            int l = this.slotHeight - 4;

            if (k > this.bottom || k + l < this.top) {
                this.func_178040_a(j, p_148120_1_, k);
            }

            if (this.showSelectionBox && this.isSelected(j)) {
                int i1 = this.left + (this.width / 2 - this.getListWidth() / 2);
                int j1 = this.left + this.width / 2 + this.getListWidth() / 2;
                ShapeUtil.INSTANCE.drawRect(i1, k - 2, j1, k + l + 2, new Color(255, 255, 255, 100).getRGB());
                GlStateManager.enableTexture2D();
            }

            this.drawSlot(j, p_148120_1_, k, l, mouseXIn, mouseYIn);
        }
    }
}
