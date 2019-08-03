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

package cc.zerite.client.injection.mixins.client.gui;

import cc.zerite.client.event.DisconnectFromServerEvent;
import cc.zerite.client.event.EventBus;
import net.minecraft.client.gui.GuiDisconnected;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiDisconnected.class)
public class MixinGuiDisconnected {

    @Inject(method = "initGui", at = @At("RETURN"))
    private void initGui(CallbackInfo ci) {
        EventBus.INSTANCE.post(new DisconnectFromServerEvent());
    }
}
