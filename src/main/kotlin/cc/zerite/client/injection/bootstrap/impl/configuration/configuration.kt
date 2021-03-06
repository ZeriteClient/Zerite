package cc.zerite.client.injection.bootstrap.impl.configuration

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class StoreConfig(val name: String = "Unknown", val hidden: Boolean = false)