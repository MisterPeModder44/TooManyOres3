package misterpemodder.tmo.main.items.tools;

import java.util.Random;

import buildcraft.api.tools.IToolWrench;
import misterpemodder.tmo.main.items.EnumItemsNames;
import misterpemodder.tmo.main.items.base.ItemBase;
import misterpemodder.tmo.main.items.materials.TmoToolMaterial;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.oredict.OreDictionary;

//TODO Fix it!
public class ItemWrench extends ItemBase implements IToolWrench {
	
	public TmoToolMaterial m;
	public boolean isUnbreakable = false;
	public boolean isAdminWrench = false;
	
	public ItemWrench(EnumItemsNames names, TmoToolMaterial material, boolean isUnbreakable, boolean isAdminWrench) {
		this(names, material);
		this.isUnbreakable = isUnbreakable;
		this.isAdminWrench = isAdminWrench;
	}

	public ItemWrench(EnumItemsNames names, TmoToolMaterial material) {
		super(names);
		this.m = material;
		this.maxStackSize = 1;
		this.setMaxDamage(this.m.material.getMaxUses());
		if(this.isEnabled()) this.setCreativeTab(TMORefs.TMO_TAB);
	}
	
	@Override
	protected void attemptSetCreativeTab() {}
	
	@Override
	public boolean canWrench(EntityPlayer player, BlockPos pos) {
		return true;
	}

	@Override
	public boolean canWrench(EntityPlayer player, Entity entity) {
		return false;
	}
	
	@Override
	public void wrenchUsed(EntityPlayer player, Entity entity) {}
	
	@Override
	public void wrenchUsed(EntityPlayer player, BlockPos pos) {
		ItemStack stack = player.getHeldItem(player.getActiveHand());
		
		if(!this.isUnbreakable) {
			stack.damageItem(1, player);
			if(stack.getItemDamage() > stack.getMaxDamage()) {
				ForgeEventFactory.onPlayerDestroyItem(player, stack, player.getActiveHand());
				player.world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1.0F, new Random().nextFloat() + 1.0F);
			}
		}
	}
	
	@Override
	public boolean isEnabled() {
		return this.m.isEnabled();
	}
	
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        ItemStack repairStack = this.m.material.getRepairItemStack();
        if (!repairStack.isEmpty() && OreDictionary.itemMatches(repairStack, repair, false)) return true;
        return super.getIsRepairable(toRepair, repair);
    }
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		if(enchantment == Enchantments.UNBREAKING) {
			return true;
		}
		return super.canApplyAtEnchantingTable(stack, enchantment);
	}
	
	public int getItemEnchantability() {
        return this.m.material.getEnchantability();
    }

}
