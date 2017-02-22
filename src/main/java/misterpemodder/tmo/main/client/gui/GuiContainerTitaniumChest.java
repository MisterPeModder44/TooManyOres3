package misterpemodder.tmo.main.client.gui;

import java.util.ArrayList;
import java.util.List;

import misterpemodder.tmo.main.client.gui.tabs.TabArmorInventory;
import misterpemodder.tmo.main.client.gui.tabs.TabBase;
import misterpemodder.tmo.main.client.gui.tabs.TabInfo;
import misterpemodder.tmo.main.client.gui.tabs.TabMainTitaniumChest;
import misterpemodder.tmo.main.client.gui.tabs.TabPlayerInventory;
import misterpemodder.tmo.main.client.gui.tabs.TabRedstone;
import misterpemodder.tmo.main.client.gui.tabs.TabSecurity;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiContainerTitaniumChest extends GuiContainerBase<ContainerTitaniumChest, TileEntityTitaniumChest>{

	public GuiContainerTitaniumChest(InventoryPlayer playerInv, TileEntityTitaniumChest te) {
		super(new ContainerTitaniumChest(te, playerInv));
	}

	@Override
	public List<TabBase> registerTabs() {
		List<TabBase> list = new ArrayList<>();
		list.add(new TabMainTitaniumChest());
		list.add(new TabInfo<ContainerTitaniumChest, TileEntityTitaniumChest>());
		list.add(new TabRedstone<ContainerTitaniumChest, TileEntityTitaniumChest>());
		
		list.add(new TabSecurity<ContainerTitaniumChest, TileEntityTitaniumChest>());
		
		list.add(new TabPlayerInventory<ContainerTitaniumChest, TileEntityTitaniumChest>());
		//Maybe added back
		//list.add(new TabIO<ContainerTitaniumChest, TileEntityTitaniumChest>());
		list.add(new TabArmorInventory<ContainerTitaniumChest, TileEntityTitaniumChest>());
		return list;
	}

}
