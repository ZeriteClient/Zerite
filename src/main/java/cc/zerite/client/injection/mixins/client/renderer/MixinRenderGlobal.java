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

package cc.zerite.client.injection.mixins.client.renderer;

import cc.zerite.client.game.cosmetics.CosmeticHandler;
import cc.zerite.client.injection.mixins.client.entity.IMixinAbstractClientPlayer;
import cc.zerite.client.injection.mixins.client.network.IMixinNetworkPlayerInfo;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

    @Inject(method = "onEntityRemoved", at = @At("HEAD"))
    private void removeEntity(Entity entityIn, CallbackInfo ci) {
        if (entityIn instanceof AbstractClientPlayer) {
            CosmeticHandler.INSTANCE.queueDelete(((AbstractClientPlayer) entityIn).getLocationCape());
            CosmeticHandler.INSTANCE.queueDelete(((AbstractClientPlayer) entityIn).getLocationSkin());
            NetworkPlayerInfo info = ((IMixinAbstractClientPlayer) entityIn).callGetPlayerInfo();
            if (info == null) return;

            ((IMixinNetworkPlayerInfo) info).setPlayerTexturesLoaded(false);
            ((IMixinNetworkPlayerInfo) info).setLocationCape(null);
            ((IMixinNetworkPlayerInfo) info).setLocationSkin(null);
        }
    }
}
