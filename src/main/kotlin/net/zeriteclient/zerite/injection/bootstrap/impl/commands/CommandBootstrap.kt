package net.zeriteclient.zerite.injection.bootstrap.impl.commands

import net.zeriteclient.zerite.event.EventBus
import net.zeriteclient.zerite.event.SendChatMessageEvent
import net.zeriteclient.zerite.event.Subscribe
import net.zeriteclient.zerite.injection.bootstrap.AbstractBootstrap
import net.zeriteclient.zerite.util.other.ReflectionUtil

class CommandBootstrap : AbstractBootstrap() {

    private val discoveredClasses: ArrayList<Class<*>> = arrayListOf()
    val commandList: ArrayList<AbstractCommand> = arrayListOf()

    override fun bootstrapTweaker() {
        discoveredClasses.addAll(ReflectionUtil.reflections!!.getSubTypesOf(AbstractCommand::class.java))
    }

    override fun bootstrapGameCreate() {
        commandList.addAll(discoveredClasses.map { it.newInstance() as AbstractCommand })

        EventBus.register(this)
    }

    @Subscribe
    private fun onChatMessageSent(e: SendChatMessageEvent) {
        val message = e.message

        if (!message.startsWith("/") || message.length <= 1) {
            return
        }

        val command = message.substring(1).split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        val trim = message.substring(command.length + 1).trim { it <= ' ' }
        val args: Array<String> =
            if (trim.isEmpty()) emptyArray() else trim.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        commandList.firstOrNull { c -> c.getInclusiveAliases().any { it.equals(command, true) } }?.let {
            it.execute(args)
            e.cancelled = true
        }
    }

}