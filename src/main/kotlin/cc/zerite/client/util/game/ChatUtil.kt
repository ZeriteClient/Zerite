package cc.zerite.client.util.game

import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting

object ChatUtil {

    private val prefix =
        EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD.toString() + "Zerite" + EnumChatFormatting.WHITE + EnumChatFormatting.BOLD + " > " + EnumChatFormatting.WHITE

    fun createComponent(message: String) = ChatComponentText("$prefix$message")

    fun printChat(message: String) =
        Minecraft.getMinecraft().thePlayer?.addChatMessage(createComponent(message))

}