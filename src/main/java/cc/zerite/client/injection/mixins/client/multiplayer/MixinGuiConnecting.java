/*
 *     Zerite Client | Open source Minecraft client modification
 *     Copyright (C) 2019  <Zerite Team>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cc.zerite.client.injection.mixins.client.multiplayer;

import cc.zerite.client.event.ConnectToServerEvent;
import cc.zerite.client.event.EventBus;
import net.minecraft.client.multiplayer.GuiConnecting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiConnecting.class)
public class MixinGuiConnecting {

    @Inject(method = "connect", at = @At("HEAD"))
    private void connect(String ip, int port, CallbackInfo ci) {
        EventBus.INSTANCE.post(new ConnectToServerEvent(ip, port));
    }
}
