package net.zeriteclient.zerite.injection.bootstrap

object ZeriteBootstrap {

    var stage: EnumStage = EnumStage.TWEAKER
        private set
    val bootstrapList: ArrayList<AbstractBootstrap> = arrayListOf()

    fun beginGameCreate() {
        stage = EnumStage.GAME_CREATE

        bootstrapList.addAll(DiscovererBootstrap.bootstraps)
        bootstrapList.forEach(AbstractBootstrap::bootstrapGameCreate)
    }

    fun beginClientInit() {
        stage = EnumStage.CLIENT_INIT
        bootstrapList.forEach(AbstractBootstrap::bootstrapClientInit)
    }

    fun beginClientShutdown() {
        stage = EnumStage.CLIENT_SHUTDOWN
        bootstrapList.forEach(AbstractBootstrap::bootstrapClientShutdown)
    }

    inline fun <reified T : AbstractBootstrap> getBootstrap(): T =
        bootstrapList.first { it.javaClass == T::class.java } as T

    fun <T : AbstractBootstrap> getBootstrap(clazz: Class<T>): T =
        bootstrapList.first { it.javaClass == clazz } as T
}