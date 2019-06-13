package net.zeriteclient.zerite.injection.bootstrap.impl.annotations

import net.zeriteclient.zerite.event.EventBus
import net.zeriteclient.zerite.injection.bootstrap.AbstractBootstrap
import net.zeriteclient.zerite.util.other.ReflectionUtil
import org.apache.logging.log4j.LogManager
import java.lang.reflect.Field
import java.lang.reflect.Modifier

class AnnotationBootstrap : AbstractBootstrap() {

    override fun bootstrapGameCreate() {
        val reflections = ReflectionUtil.reflections

        reflections!!.getTypesAnnotatedWith(Instance::class.java).forEach {
            val field = it.getDeclaredField("INSTANCE") ?: return
            field.isAccessible = true

            val obj = field.get(null) ?: return
            val annotation = it.getDeclaredAnnotation(Instance::class.java)

            if (annotation.registerEvents) {
                EventBus.register(obj)
            }

            field.declaringClass.fields.forEach { f: Field ->
                val declaringClass = f.declaringClass
                f.isAccessible = true

                if (f.isAnnotationPresent(InjectLogger::class.java) && !Modifier.isStatic(f.modifiers)) {
                    f.set(obj, LogManager.getLogger(declaringClass.simpleName))
                }
            }
        }
    }
}