package misterpemodder.tmo.main.blocks.base;

import java.util.ArrayList;
import java.util.List;

import misterpemodder.tmo.main.blocks.properties.IBlockNames;
import misterpemodder.tmo.main.blocks.properties.IBlockValues;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import scala.actors.threadpool.Arrays;

public class BlockBase extends Block implements BlockTMO {
	
	protected IBlockNames names;
	protected IBlockValues values;
	protected ItemBlockBase itemBlock;
	
	public BlockBase(IBlockNames n, IBlockValues v) {
		super(v.getMaterial(null), v.getMapColor(null));
		this.names = n;
		this.values = v;
		//names
		this.setRegistryName(names.getRegistryName());
		this.setUnlocalizedName(names.getUnlocalizedName());
		
		this.itemBlock = new ItemBlockBase(this);
		itemBlock.setRegistryName(this.getRegistryName());
		
		//values
		IBlockState defaultState = this.getDefaultState();
		this.setHardness(values.getHardness(defaultState));
		this.setResistance(values.getResistance(defaultState));
		this.setSoundType(values.getSoundType(defaultState));
		this.setLightLevel(values.getLightLevel(defaultState));
		this.setLightOpacity(values.getLightOpacity(defaultState));
		this.slipperiness = (values.getSlipperiness(defaultState));
		this.setHarvestLevel("shovel", 0);

		if(values.getUseDefaultTab() == true) setCreativeTab(TMORefs.TMO_TAB);
	}
	
	public IBlockNames getNames() {
		return this.names;
	}
	
	public IBlockValues getValues() {
		return this.values;
	}
	
	public ItemBlockBase getItemBlock() {
		return this.itemBlock;
	}
	
	@Override
	public void registerItemRender() {
		ModelResourceLocation location = new ModelResourceLocation(TMORefs.PREFIX + names.getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(this.getItemBlock(), 0, location);
	}
	
	@Override
	public void registerOreDict() {
		String[] oreDictNames = this.names.getOreDictNames();
		if(oreDictNames.length == 0) {
			return;
		}
		for (String str : oreDictNames) {
			OreDictionary.registerOre(str, this);
		}
	}
	
	public BlockRenderLayer getBlockLayer() {
		return values.getRenderLayer();
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
        return values.isFullCube(state);
    }
    
    @Override
    public int getHarvestLevel(IBlockState state) {
    	return super.getHarvestLevel(state);
    }
    
    @Override
    public String getHarvestTool(IBlockState state) {
    	return super.getHarvestTool(state);
    }
    
    @Override
    public int getLightValue(IBlockState state) {
    	return (int)(15.0F * values.getLightLevel(state));
    }
    
    @Override
    public void setHarvestLevel(String toolClass, int level) { 
        for(IBlockState state : getBlockState().getValidStates()) {
        	setHarvestLevel(values.getHarvestTool(state), values.getHarvestLevel(state), state);
        }
    }
    
    @Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
		IBlockState state = world.getBlockState(pos);
		return this.values.getResistance(state);
	}
	
	@Override
	public float getBlockHardness(IBlockState state, World worldIn, BlockPos pos) {
		return this.values.getHardness(state);
	}
	
	@Override
	public MapColor getMapColor(IBlockState state) {
		return this.values.getMapColor(state);
	}
	
	@Override
	public Material getMaterial(IBlockState state) {
		return this.values.getMaterial(state);
	}
    
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return this.isOpaqueCube(state);
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
    	return this.values != null?this.values.isOpaqueCube(state):true;
    }
    
    public boolean hasOwnItemBlock() {
    	return true;
    }
    
    @Override
    public int getLightOpacity(IBlockState state) {
    	return ((BlockBase)state.getBlock()).values.getLightOpacity(state);
    }
	
	
    @Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		if(I18n.hasKey(this.getUnlocalizedName()+".desc")) {
			String t = I18n.format(this.getUnlocalizedName()+".desc");
				
			//expand new lines
			List<String> expandedLines = Arrays.asList(t.split("\\\\n"));
				
			List<String> toAdd = new ArrayList<>();
			FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
			for(String line : expandedLines) {
				
				if(font.getStringWidth(line)>200) {
					toAdd.addAll(font.listFormattedStringToWidth(line, 200));
				} else {
					toAdd.add(line);
				}
			}
			
			for(String str : toAdd)
				tooltip.add(TextFormatting.GRAY+""+TextFormatting.ITALIC+str);
		}
	}
	
}
