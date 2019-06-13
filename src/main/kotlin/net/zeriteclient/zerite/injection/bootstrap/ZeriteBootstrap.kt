package net.zeriteclient.zerite.injection.bootstrap

object ZeriteBootstrap {

    private val bootstrapList: ArrayList<AbstractBootstrap> = arrayListOf()

    fun beginGameCreate() {
        bootstrapList.addAll(DiscovererBootstrap.bootstraps)
        bootstrapList.forEach(AbstractBootstrap::bootstrapGameCreate)
    }

    fun beginClientInit() = bootstrapList.forEach(AbstractBootstrap::bootstrapClientInit)
    fun beginClientShutdown() = bootstrapList.forEach(AbstractBootstrap::bootstrapClientShutdown)

    fun <T : AbstractBootstrap> getBootstrap(target: Class<out AbstractBootstrap>): T? =
        bootstrapList.first { it.javaClass == target } as T

}