package net.zeriteclient.zerite.injection.mixins.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.zeriteclient.zerite.game.tools.font.ZeriteFonts;
import net.zeriteclient.zerite.util.other.TimeUtil;
import net.zeriteclient.zerite.util.rendering.ShapeUtil;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;

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

    private double hoverFade;

    /**
     * @author Koding
     */
    @Overwrite
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (!visible) {
            return;
        }

        hoverFade = hovered ? Math.min(90, hoverFade + TimeUtil.INSTANCE.getDelta() / 2f) : Math.max(40, hoverFade - TimeUtil.INSTANCE.getDelta() / 2f);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition
                && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.blendFunc(770, 771);
        this.mouseDragged(mc, mouseX, mouseY);

        ShapeUtil.INSTANCE.drawFilledRoundedRectangle(xPosition, yPosition, width, height, 3,
                !this.enabled ? new Color(0, 0, 0, 100).getRGB()
                        : new Color(255, 255, 255, (int) hoverFade / 2).getRGB());
        ShapeUtil.INSTANCE.drawRoundedRectangle(xPosition, yPosition, width, height, 3, 2,
                !this.enabled ? new Color(0, 0, 0, 100).getRGB()
                        : new Color(255, 255, 255, (int) hoverFade).getRGB());

        GL11.glColor4f(1, 1, 1, 1);
        ZeriteFonts.INSTANCE.getMedium()
                .drawCenteredString(displayString.toUpperCase(),
                        xPosition + width / 2, yPosition
                                + (height - ZeriteFonts.INSTANCE.getMedium()
                                .getHeight(displayString.toUpperCase())) / 2,
                        new Color(0xFFFFFFFF).getRGB());
    }
}
