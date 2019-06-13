package net.zeriteclient.zerite.injection.bootstrap

open class AbstractBootstrap {

    open fun bootstrapTweaker() {}
    open fun bootstrapGameCreate() {}
    open fun bootstrapClientInit() {}
    open fun bootstrapClientShutdown() {}

}