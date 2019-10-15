package cc.zerite.client.game.gui.settings

import cc.zerite.client.game.gui.components.IconButton
import cc.zerite.client.game.gui.settings.tab.SettingController
import cc.zerite.client.game.gui.settings.tab.SettingGroup
import cc.zerite.client.util.EnumBackground
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.util.ResourceLocation
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse

object GuiZeriteSettings : GuiScreen() {

    private var ordered = false

    val groups = EnumSettingCategory.values().map { it to SettingGroup(it.display, it.icon) }.toMap()

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

        buttonList.add(IconButton(0, width - 22, 2, 20, 20, ResourceLocation("textures/icons/close.png")))
    }

    /**
     * Draw everything to the screen
     *
     * @param mouseX the current mouse x location
     * @param mouseY the current mouse y location
     * @param partialTicks the world tick
     */
    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        if (Minecraft.getMinecraft().theWorld == null) {
            EnumBackground.BACKGROUND_1.bind()
            Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, width, height, width.toFloat(), height.toFloat())
        }

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

    override fun actionPerformed(button: GuiButton?) {
        if (button!!.id == 0) {
            mc.displayGuiScreen(null)
        }
    }
}