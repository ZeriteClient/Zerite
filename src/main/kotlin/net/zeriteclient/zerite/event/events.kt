package net.zeriteclient.zerite.event

import java.lang.reflect.Method

abstract class Event

abstract class CancellableEvent : Event() {
    var cancelled: Boolean = false
}

data class EventSubscriber(val obj: Any, val method: Method, val eventClass: Class<*>, val priority: Int)

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Subscribe(val priority: Int)

class ClientStartEvent : Event()

class ClientShutdownEvent : Event()