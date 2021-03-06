package com.misterpemodder.tmo.mixin.client;

import javax.annotation.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.misterpemodder.tmo.hook.ItemHook;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

@Mixin(ItemRenderer.class)
public final class ItemRendererMixin {
  @Redirect(
      at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;hsvToRgb(FFF)I",
          ordinal = 0),
      method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;"
          + "Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V")
  private int modifyDurablityBarColor(float h, float s, float v, TextRenderer textRenderer,
      ItemStack stack, int int_1, int int_2, @Nullable String text) {
    return ((ItemHook) stack.getItem()).tmoGetDurabilityBarColor(stack,
        MathHelper.hsvToRgb(h, s, v));
  }
}
