package net.zeriteclient.zerite.injection.bootstrap

object ZeriteBootstrap {

    private val bootstrapList: ArrayList<AbstractBootstrap> = arrayListOf()

    fun beginGameCreate() {
        bootstrapList.addAll(DiscovererBootstrap.bootstraps)
        bootstrapList.forEach(AbstractBootstrap::bootstrapGameCreate)
    }

    fun beginClientInit() = bootstrapList.forEach(AbstractBootstrap::bootstrapClientInit)
    fun beginClientShutdown() = bootstrapList.forEach(AbstractBootstrap::bootstrapClientShutdown)

}