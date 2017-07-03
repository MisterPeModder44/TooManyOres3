package misterpemodder.tmo.main.blocks.containers;

import misterpemodder.tmo.main.blocks.base.BlockMachine;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.client.gui.GuiHandler.EnumGuiElements;
import misterpemodder.tmo.main.tileentity.TileEntityDestabilizer;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BlockDestabilizer extends BlockMachine<TileEntityDestabilizer> {
	
	public static final PropertyBool EMPTY = PropertyBool.create("empty");

	public BlockDestabilizer() {
		super(EnumBlocksNames.CRYSTAL_DESTABILIZER);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, CASING, EMPTY);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity tileentity = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
		 
		if(tileentity != null && tileentity instanceof TileEntityDestabilizer) {
			return state.withProperty(EMPTY, ((TileEntityDestabilizer)tileentity).getEnderMatterAmount() <= 0);
		}
		else {
			return super.getActualState(state, world, pos);
		}
	}


	@Override
	public Class<TileEntityDestabilizer> getTileEntityClass() {
		return TileEntityDestabilizer.class;
	}

	@Override
	public TileEntityDestabilizer createNewTileEntity(World world, IBlockState state) {
		return new TileEntityDestabilizer();
	}
	
	@Override
	protected EnumGuiElements getGuiElements() {
		return EnumGuiElements.CRYSTAL_DESTABILIZER;
	}
	
}
