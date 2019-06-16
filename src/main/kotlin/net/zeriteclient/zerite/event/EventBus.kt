package net.zeriteclient.zerite.event

import kotlin.streams.toList

object EventBus {

    private val registeredEventClasses = HashMap<Class<*>, ArrayList<EventSubscriber>>()

    fun register(obj: Any) {
        registeredEventClasses.putIfAbsent(obj.javaClass, ArrayList())

        val subscribers: ArrayList<EventSubscriber> = registeredEventClasses[obj.javaClass] ?: ArrayList()

        obj.javaClass.declaredMethods.forEach {
            if (it.getDeclaredAnnotation(Subscribe::class.java) == null || it.parameterCount != 1) {
                return@forEach
            }

            it.isAccessible = true
            subscribers.add(
                EventSubscriber(
                    obj,
                    it,
                    it.parameterTypes[0],
                    it.getDeclaredAnnotation(Subscribe::class.java).priority
                )
            )
        }

        subscribers.sortByDescending(EventSubscriber::priority)
        registeredEventClasses.replace(obj.javaClass, subscribers)
    }

    fun post(event: Event) {
        val subscribers: ArrayList<EventSubscriber> = arrayListOf()

        registeredEventClasses.forEach { _, eventSubscribers ->
            subscribers.addAll(eventSubscribers.stream()
                .filter { eventSubscriber -> eventSubscriber.eventClass === event.javaClass }
                .toList())
        }

        subscribers.forEach {
            it.method.invoke(it.obj, event)
        }
    }

}