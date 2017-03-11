package misterpemodder.tmo.main.blocks;

import misterpemodder.tmo.main.blocks.base.BlockMulti;
import misterpemodder.tmo.main.blocks.base.BlockTMO;
import misterpemodder.tmo.main.blocks.properties.IBlockVariant;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;

public class BlockTMOStairs extends BlockStairs implements BlockTMO {
	
	protected ItemBlock itemBlock;
	protected BlockMulti baseBlock;
	protected IBlockVariant variant;

	public BlockTMOStairs(BlockMulti baseBlock, IBlockVariant variant) {
		super(baseBlock.getStateFromVariant(variant));
		this.variant = variant;
		this.baseBlock = baseBlock;
		this.setUnlocalizedName(baseBlock.getUnlocalizedName().substring(5)+".stairs."+variant.getUnlocalizedName());
		this.setRegistryName(TMORefs.PREFIX + variant.getName()+"_"+baseBlock.getRegistryName().getResourcePath()+"_stairs");
		this.setLightOpacity(0);
		
		this.itemBlock = new ItemBlock(this);
		itemBlock.setRegistryName(this.getRegistryName());
		this.setCreativeTab(TMORefs.TMO_TAB);
	}

	@Override
	public void registerItemRender() {
		ModelResourceLocation location = new ModelResourceLocation(this.getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(this.getItemBlock(), 0, location);
	}

	@Override
	public void registerOreDict() {}

	@Override
	public ItemBlock getItemBlock() {
		return this.itemBlock;
	}
	
	@Override
	public MapColor getMapColor(IBlockState state) {
		return variant.getMapColor();
	}
	
	@Override
	public int getHarvestLevel(IBlockState state) {
		return super.getHarvestLevel(state);
	}
	
	@Override
	public String getHarvestTool(IBlockState state) {
		return super.getHarvestTool(state);
	}
	
}