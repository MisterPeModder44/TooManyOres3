package misterpemodder.tmo.main.events;

import buildcraft.api.tools.IToolWrench;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.blocks.BlockLamp;
import misterpemodder.tmo.main.blocks.containers.BlockContainerBase;
import misterpemodder.tmo.main.blocks.containers.BlockItemKeeper;
import misterpemodder.tmo.main.init.ModItems;
import misterpemodder.tmo.main.items.ItemLock;
import misterpemodder.tmo.main.items.ItemsVariants;
import misterpemodder.tmo.main.items.tools.ItemWrench;
import misterpemodder.tmo.main.tileentity.IOwnable;
import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import misterpemodder.tmo.main.utils.ServerUtils;
import misterpemodder.tmo.main.utils.StringUtils;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameRules.ValueType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;

@Mod.EventBusSubscriber
public class EventHandler {
	
	//Cancels the sneak effect
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void onClickBlockEvent(PlayerInteractEvent.RightClickBlock event) {
		World world = event.getWorld();
		Block b = world.getBlockState(event.getPos()).getBlock();
		ItemStack stack = event.getItemStack();
		BlockPos pos = event.getPos();
		if(b instanceof BlockItemKeeper) {
			if(((BlockItemKeeper) b).getTileEntity(world, pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, event.getFace()).getStackInSlot(0).isEmpty()) {
				event.setUseBlock(Event.Result.ALLOW);
			}
		} else if (stack.getItem() instanceof IToolWrench) {

			TileEntity tileEntity = world.getTileEntity(pos);

			EntityPlayer player = event.getEntityPlayer();
			if(player.isSneaking() && tileEntity != null) {
				if(((IToolWrench) stack.getItem()).canWrench(player, pos)) {
					((IToolWrench) stack.getItem()).wrenchUsed(player, pos);
					if(stack.getItem() instanceof ItemWrench) {
						if(((ItemWrench) stack.getItem()).isAdminWrench && tileEntity instanceof IOwnable && !world.isRemote) {
							if(ServerUtils.isOp(player)) {
								IOwnable te = (IOwnable) tileEntity;
								if (te.hasOwner()) {
									te.setOwner("");
									player.sendMessage(new TextComponentString(Tmo.proxy.translate("item.wrench.OwnerRemoved")));
									event.setCanceled(true);
									return;
								}
							} else {
								player.sendMessage(new TextComponentString(TextFormatting.RED + Tmo.proxy.translate("item.wrench.NoPermissions")));
								event.setCanceled(true);
								return;
							}
						}
					}
					
					event.setCanceled(true);
					world.setBlockToAir(pos);
					// world.spawnEntity(new EntityItem(world, pos.getX(),
					// pos.getY(), pos.getZ(), ((BlockContainerBase)
					// b).toItem((TileEntityContainerBase)tileEntity,
					// world.getBlockState(event.getPos()))));
					return;
				}
			}
			else if(!player.isSneaking() && !world.isRemote) {
				EnumFacing facing = event.getFace();
				TileEntity tets = world.getTileEntity(pos);
				if(b.rotateBlock(world, pos, facing)) {
					world.setTileEntity(pos, tets);
					((IToolWrench) stack.getItem()).wrenchUsed(player, pos);
					event.setCanceled(true);
				}
			}
		}
		
	}
		
	@SubscribeEvent
	public static void onGameOverlay(RenderGameOverlayEvent.Post event){
		//true if the screen is not drawn yet.
		if(Minecraft.getMinecraft().currentScreen == null && event.getType() == RenderGameOverlayEvent.ElementType.ALL) {

			Minecraft mc = Minecraft.getMinecraft();
			RayTraceResult hitPos = mc.objectMouseOver;
					
			if(hitPos.getBlockPos() != null) {
				IBlockState state = mc.world.getBlockState(hitPos.getBlockPos());
				if(state.getBlock() instanceof BlockLamp) {
					EntityPlayer player = mc.player;
					String txt = state.getValue(BlockLamp.INVERTED)? TextFormatting.RED+Tmo.proxy.translate("tile.blockLamp.mode.inverted") : TextFormatting.GREEN+Tmo.proxy.translate("tile.blockLamp.mode.normal");
					ScaledResolution res = event.getResolution();
					int h = res.getScaledHeight()/20;
					StringUtils.drawCenteredString(mc.fontRendererObj, TextFormatting.BOLD+""+TextFormatting.UNDERLINE+Tmo.proxy.translate("tile.blockLamp.mode.title")+TextFormatting.RESET+" "+TextFormatting.BOLD+""+txt, res.getScaledWidth()/2, res.getScaledHeight()/2 + h);
					StringUtils.drawCenteredString(mc.fontRendererObj, TextFormatting.GRAY+""+TextFormatting.ITALIC+Tmo.proxy.translate("tile.blockLamp.mode.hint"), res.getScaledWidth()/2, res.getScaledHeight()/2 + 2*h);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void anvilUpdateEvent(AnvilUpdateEvent event) {
		
		if(event.getLeft().getItem() == ModItems.Items.LOCK.getItem()) {
			ItemStack lStack = event.getLeft();
			ItemStack rStack = event.getRight();
			ItemsVariants.Lock variant = ((ItemLock)lStack.getItem()).getVariant(lStack.getMetadata());
			ItemStack repairItem = variant.getRepairItem();
			
			if(rStack.isItemEqual(repairItem) && variant.isBroken() && rStack.getCount() >= repairItem.getCount()) {
				ItemStack output = variant.getOtherVariant();
				
				if(lStack.hasTagCompound()) {
					output.setTagCompound(lStack.getTagCompound());
				}
				
				event.setMaterialCost(repairItem.getCount());
				event.setOutput(output);
				event.setCost(variant.getCost());
			} 
			else if(rStack.getItem() == net.minecraft.init.Items.ENCHANTED_BOOK) {
				ItemEnchantedBook book = (ItemEnchantedBook)rStack.getItem();
				NBTTagList enchants = book.getEnchantments(rStack);
				boolean valid = false;
				
				if (enchants != null) {
		            for (int i = 0; i < enchants.tagCount(); ++i) {
		                int id = enchants.getCompoundTagAt(i).getShort("id");
		                int lvl = enchants.getCompoundTagAt(i).getShort("lvl");
		                if (Enchantment.getEnchantmentByID(id) == Enchantments.UNBREAKING) {
		                   valid = true;
		                }
		            }
		        }
				
				if(!valid) event.setCanceled(true);
				
			}
		}
	}
	
	
	@SubscribeEvent
	public static void worldLoadingEvent(WorldEvent.Load event) {
		if(!event.getWorld().getGameRules().hasRule("dropInvContents")) {
			event.getWorld().getGameRules().addGameRule("dropInvContents", "true", ValueType.BOOLEAN_VALUE);
		}
	}
	
	@SubscribeEvent
	public static void blockBreakEvent(BlockEvent.BreakEvent event) {
		TileEntity tileEntity = event.getWorld().getTileEntity(event.getPos());
		if(tileEntity != null && tileEntity instanceof IOwnable) {
			IOwnable te = (IOwnable) tileEntity;
			if(te.hasOwner()) {
				if(event.getPlayer().getName() != te.getOwner()) {
					event.setCanceled(true);
				}
			}
		}
	}
	
}
