package net.zeriteclient.zerite.game.gui.settings

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.util.ResourceLocation
import net.zeriteclient.zerite.game.gui.settings.element.impl.SettingsButton
import net.zeriteclient.zerite.game.gui.settings.tab.SettingController
import net.zeriteclient.zerite.game.gui.settings.tab.SettingDropdown
import net.zeriteclient.zerite.game.gui.settings.tab.SettingGroup
import org.lwjgl.input.Keyboard
import java.io.IOException

class GuiZeriteSettings : GuiScreen() {

    /**
     * The controller for what elements are added to a menu
     */
    private var controller: SettingController

    init {
        val groups = arrayListOf<SettingGroup>()

        for (i in 0..9) {
            val dropdowns = arrayListOf<SettingDropdown>()

            for (j in 0..9) {
                dropdowns.add(
                    SettingDropdown(
                        "Dropdown $j", arrayListOf(
                            SettingsButton("Button", onClick = {
                                println("TEST")
                            })
                        )
                    )
                )
            }

            groups.add(SettingGroup("Group $i", ResourceLocation("textures/icons/info.png"), dropdowns))
        }

        controller = SettingController(groups, groups[0])
    }

    /**
     * Initialize the Zerite GUI
     */
    override fun initGui() {
        val scaledResolution = ScaledResolution(Minecraft.getMinecraft())

        this.buttonList.add(
            GuiButton(
                1, scaledResolution.scaledWidth / 2 - 40,
                scaledResolution.scaledHeight - 20, 80, 20, "Back"
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
        drawDefaultBackground()
//        BackgroundUtils.drawBG()

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