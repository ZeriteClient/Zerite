package cc.zerite.client.injection.bootstrap.impl.commands

import cc.zerite.client.event.EventBus
import cc.zerite.client.event.SendChatMessageEvent
import cc.zerite.client.event.Subscribe
import cc.zerite.client.injection.bootstrap.AbstractBootstrap
import cc.zerite.client.util.other.ReflectionUtil

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