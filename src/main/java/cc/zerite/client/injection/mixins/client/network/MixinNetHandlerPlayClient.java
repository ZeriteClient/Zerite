package cc.zerite.client.injection.mixins.client.network;

import cc.zerite.client.injection.bootstrap.ZeriteBootstrap;
import cc.zerite.client.injection.bootstrap.impl.commands.AbstractCommand;
import cc.zerite.client.injection.bootstrap.impl.commands.CommandBootstrap;
import cc.zerite.client.injection.mixinsimp.client.network.MixinNetHandlerPlayClientImpl;
import cc.zerite.client.util.other.ReflectionUtil;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.command.CommandBase;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S3APacketTabComplete;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {

    private Minecraft gameController;

    private MixinNetHandlerPlayClientImpl impl = new MixinNetHandlerPlayClientImpl((NetHandlerPlayClient) (Object) this);

    @Inject(method = "handleChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;printChatMessage(Lnet/minecraft/util/IChatComponent;)V", shift = At.Shift.BEFORE), cancellable = true)
    private void handleChat(S02PacketChat packetIn, CallbackInfo ci) {
        impl.handleChat(packetIn, ci);
    }

    /**
     * @author Koding
     */
    @Overwrite
    public void handleTabComplete(S3APacketTabComplete packetIn) {
        // Check thread
        PacketThreadUtil
                .checkThreadAndEnqueue(packetIn, Minecraft.getMinecraft().thePlayer.sendQueue,
                        this.gameController);

        // Create basic options
        List<String> options = new ArrayList<>(Arrays.asList(packetIn.func_149630_c()));

        try {
            // Check if chat GUI
            if (Minecraft.getMinecraft().currentScreen instanceof GuiChat) {
                // Get input field
                Field inputField = ReflectionUtil
                        .INSTANCE.getField(Minecraft.getMinecraft().currentScreen.getClass(),
                                new String[]{"a", "inputField"});

                // Check if present
                if (inputField != null) {
                    // Set accessible
                    inputField.setAccessible(true);

                    // Get field
                    GuiTextField input = (GuiTextField) inputField
                            .get(Minecraft.getMinecraft().currentScreen);

                    // Get command bootstrap
                    CommandBootstrap cb = ZeriteBootstrap.INSTANCE.getBootstrap(CommandBootstrap.class);

                    // Create strings
                    String[] aString = input.getText().substring(1).split(" ", -1);
                    String s = aString[0];

                    // Check if bootstrap is present
                    if (cb != null) {
                        // Check length
                        if (aString.length == 1) {
                            // Create list
                            List<String> list = Lists.newArrayList();

                            // Loop through commands
                            for (AbstractCommand c : cb.getCommandList()) {
                                // Loop through inclusive aliases
                                for (String alias : c.getInclusiveAliases()) {
                                    // Check if string starts with
                                    if (CommandBase.doesStringStartWith(s, alias)) {
                                        // Add command
                                        list.add("/" + alias);
                                    }
                                }
                            }

                            // Add all
                            options.addAll(list);
                        } else {
                            // Loop through commands
                            for (AbstractCommand c : cb.getCommandList()) {
                                // Loop through all aliases
                                for (String alias : c.getInclusiveAliases()) {
                                    // Check if string starts with
                                    if (CommandBase.doesStringStartWith(s, alias)) {
                                        // Check for chat GUI
                                        if (this.gameController.currentScreen instanceof GuiChat) {
                                            // Cast from current screen
                                            GuiChat guichat = (GuiChat) this.gameController.currentScreen;

                                            String[] droppedStr = new String[aString.length - 1];
                                            System.arraycopy(aString, 1, droppedStr, 0, aString.length - 1);

                                            // Return response
                                            guichat.onAutocompleteResponse(c.addTabCompletionOptions(droppedStr, Minecraft.getMinecraft().thePlayer.playerLocation));
                                        }
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // Fallback
        if (this.gameController.currentScreen instanceof GuiChat) {
            GuiChat guichat = (GuiChat) this.gameController.currentScreen;
            guichat.onAutocompleteResponse(options.toArray(new String[0]));
        }
    }

}
