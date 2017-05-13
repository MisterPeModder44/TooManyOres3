package misterpemodder.tmo.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.client.gui.ContainerDestabilizer;
import misterpemodder.tmo.main.client.gui.ContainerMachine;
import misterpemodder.tmo.main.client.gui.GuiTank;
import misterpemodder.tmo.main.client.gui.RecipeClickableAreaTMO;
import misterpemodder.tmo.main.client.gui.slot.IHidable;
import misterpemodder.tmo.main.client.gui.slot.SlotHidable;
import misterpemodder.tmo.main.compat.jei.injector.RecipeCategoryInjector;
import misterpemodder.tmo.main.tileentity.TileEntityDestabilizer;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.items.IItemHandler;

public class TabMainDestabilizer extends TabMain<ContainerDestabilizer, TileEntityDestabilizer>{
	
	private GuiTank tank;

	@Override
	public TabID getTabID() {
		return TabID.MAIN_DESTABILIZER;
	}

	@Override
	public TabTexture getTabTexture() {
		return new TabTexture(DEFAULT_TAB_LOCATION, new Point(0,0), new Point(32, 0), new ResourceLocationTmo("textures/gui/container/destabilizer_main.png"), new Dimension(212, 100));
	}
	
	@Override
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		if(this.tank != null) {
			this.tank.drawTank(mouseX, mouseY);
		}
		
		GlStateManager.enableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocationTmo("textures/gui/container/destabilizer_main.png"));
		Gui.drawModalRectWithCustomSizedTexture(guiContainer.getGuiLeft()+115, guiContainer.getGuiTop()+39, 0, 100, 28, 21, 256, 128);
		int p = ((ContainerMachine)this.guiContainer.container).progress;
		if(p > 0) {
			Gui.drawModalRectWithCustomSizedTexture(guiContainer.getGuiLeft()+115, guiContainer.getGuiTop()+39, 29, 100, p, 21, 256, 128);
		}
		
		TileEntityDestabilizer te = (TileEntityDestabilizer) guiContainer.container.getTileEntity();
		
		if(te != null && te.getEnderMatterAmount() > 0) {
			Gui.drawModalRectWithCustomSizedTexture(guiContainer.getGuiLeft()+12, guiContainer.getGuiTop()+83, 73, 101, (te.getEnderMatterAmount()*138)/TileEntityDestabilizer.MAX_ENDER_MATTER, 6, 256, 128);
		}
		
		GlStateManager.disableBlend();
		
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		List<String> strs = this.tank != null? this.tank.getHoverDesc(mouseX, mouseY) : new ArrayList<String>();
		
		if(strs.isEmpty() && guiContainer.isPointInRegion(12, 83, 138, 6, mouseX, mouseY)) {
			TileEntityDestabilizer te = (TileEntityDestabilizer) guiContainer.container.getTileEntity();
			if(te != null) {
				strs.add(Tmo.proxy.translate("gui.bar.ender")+": "+te.getEnderMatterAmount());
			}
		}
		
		if(!strs.isEmpty()) {
			GuiUtils.drawHoveringText(strs, mouseX-guiContainer.getGuiLeft(), mouseY-guiContainer.getGuiTop(), guiContainer.width, guiContainer.height, 200, guiContainer.getFontRenderer());
		}

	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if(this.tank == null || !this.tank.mouseClicked(mouseX, mouseY, mouseButton)) {
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	public void initButtons(int topX, int topY) {
		this.tank = new GuiTank(0, ((TileEntityDestabilizer)guiContainer.container.getTileEntity()).getTank(), guiContainer, 161, 9);
	}
	
	@Override
	public boolean shouldDisplaySlot(IHidable slot) {
		if(slot instanceof SlotHidable && guiContainer.container.getTileEntity() instanceof TileEntityDestabilizer) {
			TileEntityDestabilizer te = (TileEntityDestabilizer)guiContainer.container.getTileEntity();
			IItemHandler h = ((SlotHidable)slot).getItemHandler();
			return h == te.getInventory() || h == te.getEnder();
		}
		return false;
	}
	
	@Override
	public boolean hasRecipeClickableAreas() {
		return true;
	}
	
	@Override
	public RecipeClickableAreaTMO[] getRecipeClickableAreas() {
		return new RecipeClickableAreaTMO[]{new RecipeClickableAreaTMO(guiContainer.getGuiTop()+115, guiContainer.getGuiTop()+39, guiContainer.getGuiLeft()+143, guiContainer.getGuiLeft()+60, RecipeCategoryInjector.UID)};
	}

}