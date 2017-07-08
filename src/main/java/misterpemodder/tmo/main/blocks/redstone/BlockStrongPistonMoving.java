package misterpemodder.tmo.main.blocks.redstone;

import javax.annotation.Nullable;

import misterpemodder.tmo.main.blocks.base.IBlockTMO;
import misterpemodder.tmo.main.blocks.base.IBlockTileEntity;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.tileentity.TileEntityStrongPiston;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;

public class BlockStrongPistonMoving extends BlockPistonMoving implements IBlockTMO, IBlockTileEntity<TileEntityStrongPiston> {
	
	public BlockStrongPistonMoving() {
		super();
		this.setUnlocalizedName(EnumBlocksNames.STRONG_PISTON_MOVING.getUnlocalizedName());
		this.setRegistryName(TMORefs.PREFIX + EnumBlocksNames.STRONG_PISTON_MOVING.getRegistryName());
		this.setLightOpacity(0);
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}
	
	@Override
	public boolean hasOwnItemBlock() {
		return false;
	}
	
	@Override
	public Class<TileEntityStrongPiston> getTileEntityClass() {
		return TileEntityStrongPiston.class;
	}
	
	public static <T extends TileEntity> TileEntity createTilePiston(IBlockState blockStateIn, @Nullable T tileEntityIn, EnumFacing facingIn, boolean extendingIn, boolean shouldHeadBeRenderedIn) {
        return new TileEntityStrongPiston(blockStateIn, tileEntityIn, facingIn, extendingIn, shouldHeadBeRenderedIn);
    }

}