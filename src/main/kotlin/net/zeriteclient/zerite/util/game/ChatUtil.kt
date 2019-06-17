package net.zeriteclient.zerite.util.game

import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting

object ChatUtil {

    private val prefix =
        EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD.toString() + "Zerite" + EnumChatFormatting.WHITE + EnumChatFormatting.BOLD + " > " + EnumChatFormatting.WHITE

    fun printChat(message: String) =
        Minecraft.getMinecraft().thePlayer?.addChatMessage(ChatComponentText("$prefix$message"))

}