package misterpemodder.tmo.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;
import java.util.List;

import org.apache.commons.lang3.tuple.MutablePair;

import misterpemodder.tmo.main.client.gui.ContainerBase;
import misterpemodder.tmo.main.client.gui.GuiContainerBase;
import misterpemodder.tmo.main.client.gui.SlotHidable;
import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class TabIO<C extends ContainerBase<TE>, TE extends TileEntityContainerBase> extends TabBase {

	public TabIO() {
		super(TabPos.BOTTOM_RIGHT);
	}
	
	@Override
	public MutablePair<TabBase,TabBase> forceTabConfig() {
		//TODO WTF ECLIPSE ???
		for(TabBase tab : (List<TabBase>)guiContainer.getRegisteredTabs()) {
			if(tab instanceof TabMain) {
				return MutablePair.of(tab, (TabBase) this);
			}
		}
		return super.forceTabConfig();
	}

	@Override
	public String getUnlocalizedName() {
		return "gui.tab.io.name";
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemStack(Blocks.HOPPER);
	}

	@Override
	public TabTexture getTabTexture() {
		return new TabTexture(DEFAULT_TAB_LOCATION, new Point(0,56), new Point(32, 56), new ResourceLocationTmo("textures/gui/container/io.png"), new Dimension(212, 100));
	}
	
	@Override
	public boolean shouldDisplaySlot(SlotHidable slot) {
		return false;
	}

}
