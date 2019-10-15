package cc.zerite.client.injection.mixins.client.gui;

import cc.zerite.client.game.gui.components.custom.CustomWavedButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiButton.class)
public abstract class MixinGuiButton extends Gui {

    @Shadow protected int width;
    @Shadow protected int height;
    @Shadow public int xPosition;
    @Shadow public int yPosition;
    @Shadow public String displayString;
    @Shadow public boolean visible;
    @Shadow protected boolean hovered;
    @Shadow protected abstract void mouseDragged(Minecraft mc, int mouseX, int mouseY);
    @Shadow public boolean enabled;

    private CustomWavedButton customButton = new CustomWavedButton("", false, true, 3, 0, () -> null);

    /**
     * @author Koding
     */
    @Overwrite
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (!visible) {
            return;
        }

        customButton.setEnabled(enabled);
        customButton.setLabel(this.displayString);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.mouseDragged(mc, mouseX, mouseY);

        customButton.drawButton(xPosition, yPosition, width, height);
    }

    /**
     * @author Koding
     */
    @Inject(method = "mousePressed", at = @At("RETURN"))
    private void mousePressed(Minecraft mc, int mouseX, int mouseY, CallbackInfoReturnable<Boolean> ci) {
        customButton.mousePressed(mouseX, mouseY);
    }
}
