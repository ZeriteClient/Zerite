package net.zeriteclient.zerite.injection.bootstrap

object ZeriteBootstrap {

    val bootstrapList: ArrayList<AbstractBootstrap> = arrayListOf()

    fun beginGameCreate() {
        bootstrapList.addAll(DiscovererBootstrap.bootstraps)
        bootstrapList.forEach(AbstractBootstrap::bootstrapGameCreate)
    }

    fun beginClientInit() = bootstrapList.forEach(AbstractBootstrap::bootstrapClientInit)
    fun beginClientShutdown() = bootstrapList.forEach(AbstractBootstrap::bootstrapClientShutdown)

    inline fun <reified T : AbstractBootstrap> getBootstrap(): T =
        bootstrapList.first { it.javaClass == T::class.java } as T

    fun <T : AbstractBootstrap> getBootstrap(clazz: Class<T>): T =
        bootstrapList.first { it.javaClass == clazz } as T

}