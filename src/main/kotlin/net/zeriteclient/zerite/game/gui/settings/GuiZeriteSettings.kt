package net.zeriteclient.zerite.game.gui.settings

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.util.ResourceLocation
import net.zeriteclient.zerite.game.gui.settings.tab.SettingController
import net.zeriteclient.zerite.game.gui.settings.tab.SettingGroup
import net.zeriteclient.zerite.util.EnumBackground
import net.zeriteclient.zerite.util.rendering.ShapeUtil
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import java.awt.Color
import java.io.IOException

object GuiZeriteSettings : GuiScreen() {

    private var ordered = false

    val groups = hashMapOf(
        EnumSettingCategory.MODS to SettingGroup("Mods", ResourceLocation("textures/icons/settings.png"))
    )

    /**
     * The controller for what elements are added to a menu
     */
    private var controller: SettingController

    init {
        controller = SettingController(ArrayList(groups.values))
    }

    /**
     * Initialize the Zerite GUI
     */
    override fun initGui() {
        if (!ordered) {
            ordered = true

            groups.forEach { it.value.dropDowns.sortByDescending { v -> v.name } }
        }

        val scaledResolution = ScaledResolution(Minecraft.getMinecraft())

        this.buttonList.add(
            GuiButton(
                1, scaledResolution.scaledWidth - 105,
                scaledResolution.scaledHeight - 25, 80, 20, "Back"
            )
        )
    }

    /**
     * Draw everything to the screen
     *
     * @param mouseX the current mouse x location
     * @param mouseY the current mouse y location
     * @param partialTicks the world tick
     */
    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (Minecraft.getMinecraft().theWorld == null) {
            EnumBackground.BACKGROUND_1.bind()
            Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, width, height, width.toFloat(), height.toFloat())
        } else {
            ShapeUtil.drawRectWithSize(0, 0, width, height, Color(0, 0, 0, 100).rgb)
        }

        ShapeUtil.drawGradientRect(
            0.0,
            0.0,
            width.toDouble(),
            height.toDouble(),
            Color(3, 169, 244, 20).rgb,
            Color(2, 136, 209, 130).rgb
        )

        controller.draw()

        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    /**
     * If the player presses escape, exit the menu
     *
     * @param typedChar the pressed character
     * @param keyCode the key number of the pressed character
     */
    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            Minecraft.getMinecraft().displayGuiScreen(null)
        }
    }

    override fun handleMouseInput() {
        if (Mouse.getEventDWheel() != 0)
            controller.mouseScrolled(Mouse.getEventDWheel())

        super.handleMouseInput()
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        controller.mouseClicked(mouseX, mouseY, mouseButton)
        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    /**
     * When the player clicks a button, what will it do
     *
     * @param button the button being clicked
     * @throws IOException the exception that's thrown if something goes wrong
     */
    @Throws(IOException::class)
    override fun actionPerformed(button: GuiButton) {
        if (button.id == 1) {
            Minecraft.getMinecraft().displayGuiScreen(null)
        }
        super.actionPerformed(button)
    }

}