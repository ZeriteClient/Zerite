package net.zeriteclient.zerite.injection.bootstrap.impl.commands

import net.minecraft.util.BlockPos

abstract class AbstractCommand(private val name: String, private val usage: String, private val aliases: Array<String> = emptyArray()) {

    abstract fun execute(args: Array<String>)

    open fun addTabCompletionOptions(args: Array<String>, pos: BlockPos) = emptyArray<String>()

    fun getInclusiveAliases(): Array<String> {
        val aliases = ArrayList<String>()
        aliases.add(name)
        aliases.addAll(this.aliases)
        return aliases.toTypedArray()
    }

}