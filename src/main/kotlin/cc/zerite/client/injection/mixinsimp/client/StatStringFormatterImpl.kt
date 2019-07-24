package cc.zerite.client.injection.mixinsimp.client

import net.minecraft.client.Minecraft
import net.minecraft.client.settings.GameSettings
import net.minecraft.stats.IStatStringFormat

class StatStringFormatterImpl : IStatStringFormat {

    /**
     * Formats the strings based on 'IStatStringFormat' interface.
     *
     * @param str The String to format
     */
    override fun formatString(str: String): String {
        return try {
            String.format(
                str, GameSettings.getKeyDisplayString(
                    Minecraft.getMinecraft().gameSettings.keyBindInventory.keyCode
                )
            )
        } catch (exception: Exception) {
            "Error: " + exception.localizedMessage
        }

    }

}