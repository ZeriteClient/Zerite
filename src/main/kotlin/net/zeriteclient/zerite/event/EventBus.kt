package net.zeriteclient.zerite.event

import kotlin.streams.toList

object EventBus {

    private val registeredEventClasses = HashMap<Class<*>, ArrayList<EventSubscriber>>()

    fun register(obj: Any) {
        registeredEventClasses.putIfAbsent(obj.javaClass, ArrayList())

        val subscribers: ArrayList<EventSubscriber> = registeredEventClasses[obj.javaClass] ?: ArrayList()

        for (method in obj.javaClass.declaredMethods) {
            if (method.getDeclaredAnnotation(Subscribe::class.java) == null || method.parameterCount != 1) {
                continue
            }

            method.isAccessible = true
            subscribers.add(
                EventSubscriber(
                    obj,
                    method,
                    method.parameterTypes[0],
                    method.getDeclaredAnnotation(Subscribe::class.java).priority
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

        for (subscriber in subscribers) {
            subscriber.method.invoke(subscriber.obj, event)
        }
    }

}