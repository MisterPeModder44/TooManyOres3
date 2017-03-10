package misterpemodder.tmo.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.Arrays;

import org.lwjgl.input.Keyboard;

import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.gui.recipes.RecipeClickableArea;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.client.gui.ContainerTitaniumAnvil;
import misterpemodder.tmo.main.client.gui.GuiContainerBase;
import misterpemodder.tmo.main.client.gui.slot.IHidable;
import misterpemodder.tmo.main.client.gui.slot.SlotHidable;
import misterpemodder.tmo.main.network.PacketDataHandlers;
import misterpemodder.tmo.main.network.TMOPacketHandler;
import misterpemodder.tmo.main.network.packet.PacketClientToServer;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumAnvil;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.client.config.GuiUtils;


//Contains a lot of vanilla code from GuiRepair, it is VERY messy
public class TabMainTitaniumAnvil extends TabMain<ContainerTitaniumAnvil, TileEntityTitaniumAnvil> implements IContainerListener {
	
	private ContainerTitaniumAnvil anvil;
    private GuiTextField nameField;
    
    public TabMainTitaniumAnvil() {
    	super();
	}
    
    @Override
    public void setGuiContainer(GuiContainerBase guiContainer) {
    	super.setGuiContainer(guiContainer);
    	this.anvil = (ContainerTitaniumAnvil)this.guiContainer.container;
    }
	
	@Override
	public TabID getTabID() {
		return TabID.MAIN_TANVIL;
	}

	@Override
	public TabTexture getTabTexture() {
		return new TabTexture(DEFAULT_TAB_LOCATION, new Point(0,0), new Point(32, 0), new ResourceLocationTmo("textures/gui/container/anvil_main.png"), new Dimension(212, 100));
	}
	
	public void initButtons(int topX, int topY) {
        Keyboard.enableRepeatEvents(true);
        
        final int i = (guiContainer.width - guiContainer.getXSize()) / 2;
        final int j = (guiContainer.height - guiContainer.getYSize()) / 2;
        
        this.nameField = new GuiTextField(0, guiContainer.getFontRenderer(), i+80, j+30, 103, 12) {
        	@Override
        	//Dirty workaround to make to textbox draw in the right place
        	public void drawTextBox() {
        		this.xPosition = 80;
        		this.yPosition = 30;
        		super.drawTextBox();
        		this.xPosition += i;
        		this.yPosition += j;
        	}
        };
        this.nameField.setTextColor(14737632);
        this.nameField.setDisabledTextColour(7368816);
        this.nameField.setEnableBackgroundDrawing(false);
        this.nameField.setMaxStringLength(31);
        this.anvil.removeListener(this);
        this.anvil.addListener(this);
    }
	
	public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        this.anvil.removeListener(this);
    }
	
	@Override
	public boolean shouldDisplaySlot(IHidable slot) {
		if(slot instanceof SlotHidable) {
			ContainerTitaniumAnvil c = (ContainerTitaniumAnvil) guiContainer.container;
			return ((SlotHidable)slot).getItemHandler() == c.input || ((SlotHidable)slot).getItemHandler() == c.output || super.shouldDisplaySlot(slot);
		}
		return false;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
	}
	
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		boolean flag = true;
		ContainerTitaniumAnvil c = (ContainerTitaniumAnvil)guiContainer.container;
		if(((TileEntityTitaniumAnvil)c.getTileEntity()).getInventory().getStackInSlot(0).isEmpty()) {
			flag = false;
			guiContainer.getMinecraft().getTextureManager().bindTexture(new ResourceLocationTmo("textures/items/empty_hammer_slot.png"));
			Gui.drawModalRectWithCustomSizedTexture(guiContainer.getGuiLeft()+45, guiContainer.getGuiTop()+26, 0, 0, 16, 16, 16, 16);
		}
		guiContainer.getMinecraft().getTextureManager().bindTexture(getTabTexture().screenTexture);
		if(c.output.getStackInSlot(0).isEmpty() || !flag) {
			Gui.drawModalRectWithCustomSizedTexture(guiContainer.getGuiLeft()+77, guiContainer.getGuiTop()+26, 110F, 100F, 110, 16, 256F, 128F);
			Gui.drawModalRectWithCustomSizedTexture(guiContainer.getGuiLeft()+117, guiContainer.getGuiTop()+51, 212F, 0, 28, 21, 256F, 128F);
			this.nameField.setTextColor(7368816);
		} else {
			Gui.drawModalRectWithCustomSizedTexture(guiContainer.getGuiLeft()+77, guiContainer.getGuiTop()+26, 0F, 100F, 110, 16, 256F, 128F);
			this.nameField.setTextColor(14737632);
		}
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		
		GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.nameField.drawTextBox();
		
		guiContainer.getFontRenderer().drawString(Tmo.proxy.translate("gui.tab.titaniumAnvil.name"), 8, 6, 4210752);
		if(guiContainer.isPointInRegion(45, 26, 16, 16, mouseX, mouseY) && !guiContainer.container.getSlot(41).getHasStack()) {
    		GuiUtils.drawHoveringText(Arrays.asList(Tmo.proxy.translate("gui.slot.hammer.name")), mouseX-guiContainer.getGuiLeft(), mouseY-guiContainer.getGuiTop(), guiContainer.width, guiContainer.height, 200, guiContainer.getFontRenderer());
	    }

        int maximumCost = ((ContainerTitaniumAnvil)guiContainer.container).maximumCost;
        if (maximumCost > 0) {
			int i = 8453920;
        	boolean flag = true;
        	String str = Tmo.proxy.translate("container.repair.cost", maximumCost);

        	if (maximumCost >= 55 && !guiContainer.container.player.capabilities.isCreativeMode) {
            	str = Tmo.proxy.translateFormatted("%s (%d)", "container.repair.expensive", maximumCost);
            	i = 16736352;
        	}
        	else if (!guiContainer.container.getSlot(44).getHasStack()) {
            	flag = false;
        	}
        	else if (!guiContainer.container.getSlot(44).canTakeStack(guiContainer.container.player)) {
            	i = 16736352;
        	}

        	if (flag) {
            	int j = -16777216 | (i & 16579836) >> 2 | i & -16777216;
            	int k = guiContainer.getXSize() - 60 - guiContainer.getFontRenderer().getStringWidth(str);

            	if (guiContainer.getFontRenderer().getUnicodeFlag()) {
            		Gui.drawRect(k - 3, 65, guiContainer.getXSize() - 7, 87, -16777216);
                	Gui.drawRect(k - 2, 66, guiContainer.getXSize() - 8, 86, -12895429);
            	}
            	else {
            		guiContainer.getFontRenderer().drawString(str, k, 78, j);
            		guiContainer.getFontRenderer().drawString(str, k + 1, 77, j);
            		guiContainer.getFontRenderer().drawString(str, k + 1, 78, j);
            	}

            	guiContainer.getFontRenderer().drawString(str, k, 77, i);
        	}
        }
	}
	
	public boolean keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.nameField.textboxKeyTyped(typedChar, keyCode)) {
            this.renameItem();
            return true;
        }
        return false;
    }

    private void renameItem() {
        String s = this.nameField.getText();
        Slot slot = this.anvil.getSlot(0);

        if (slot != null && slot.getHasStack() && !slot.getStack().hasDisplayName() && s.equals(slot.getStack().getDisplayName())) {
            s = "";
        }

        this.anvil.updateItemName(s);
        NBTTagCompound toSend = new NBTTagCompound();
        toSend.setString("item_name", s);
        toSend.setInteger("world_dim_id", Minecraft.getMinecraft().world.provider.getDimension());
		toSend.setTag("player_id", NBTUtil.createUUIDTag(Minecraft.getMinecraft().player.getUniqueID()));
		toSend.setTag("input_item", this.anvil.input.getStackInSlot(0).serializeNBT());
		TMOPacketHandler.network.sendToServer(new PacketClientToServer(PacketDataHandlers.ANVIL_ITEM_NAME_HANDLER, toSend));
    }
    
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
    }
	
	@Override
	public boolean hasRecipeClickableAreas() {
		return true;
	}
	
	@Override
	public RecipeClickableArea[] getRecipeClickableAreas() {
		return new RecipeClickableArea[]{new RecipeClickableArea(guiContainer.getGuiTop()+54, guiContainer.getGuiTop()+69, guiContainer.getGuiLeft()+120, guiContainer.getGuiLeft()+142, VanillaRecipeCategoryUid.ANVIL)};
	}
	
	@Override
	public void updateCraftingInventory(Container containerToSend, NonNullList<ItemStack> itemsList){
        this.sendSlotContents(containerToSend, 42, containerToSend.getSlot(42).getStack());
    }
	
	@Override
    public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
        if (slotInd == 42) {
        	if(this.nameField.getText().isEmpty())
            this.nameField.setText(stack.isEmpty()? "" : stack.getDisplayName());
            this.nameField.setEnabled(!stack.isEmpty());

            if (!stack.isEmpty()) {
                this.renameItem();
            }
        }
    }
	
    @Override
	public void sendProgressBarUpdate(Container containerIn, int varToUpdate, int newValue){}

    @Override
    public void sendAllWindowProperties(Container containerIn, IInventory inventory){}

}