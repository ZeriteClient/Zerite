package cc.zerite.client.injection.mixins.block;

import cc.zerite.client.game.features.integrations.clearglass.ClearGlass;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockGlass.class)
public class MixinBlockGlass extends Block {

    public MixinBlockGlass(Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
    }

    /**
     * @author asbyth
     * @reason disable glass when turned on
     */
    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        if (!ClearGlass.INSTANCE.getClearGlass()) {
            return super.shouldSideBeRendered(worldIn, pos, side);
        }

        return false;
    }
}
