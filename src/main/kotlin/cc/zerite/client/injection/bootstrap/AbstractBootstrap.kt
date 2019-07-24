package cc.zerite.client.injection.bootstrap

open class AbstractBootstrap(var priority: Int = 0) {

    open fun bootstrapTweaker() {}
    open fun bootstrapGameCreate() {}
    open fun bootstrapClientInit() {}
    open fun bootstrapClientShutdown() {}

}