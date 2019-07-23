package net.zeriteclient.zerite.injection.mixins.client.network.play.server;

import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.zeriteclient.zerite.injection.mixinsimp.client.network.play.server.MixinS2EPacketCloseWindowImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(S2EPacketCloseWindow.class)
public class MixinS2EPacketCloseWindow {

    private MixinS2EPacketCloseWindowImpl impl = new MixinS2EPacketCloseWindowImpl((S2EPacketCloseWindow) (Object) this);

    @Inject(method = "processPacket", at = @At("HEAD"), cancellable = true)
    private void processPacket(INetHandlerPlayClient handler, CallbackInfo ci) {
        impl.processPacket(handler, ci);
    }

}
