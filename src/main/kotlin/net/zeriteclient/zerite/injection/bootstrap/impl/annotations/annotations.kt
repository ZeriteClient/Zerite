package net.zeriteclient.zerite.injection.bootstrap.impl.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class InjectLogger

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Instance(val registerEvents: Boolean = false, val registerConfig: Boolean = false)