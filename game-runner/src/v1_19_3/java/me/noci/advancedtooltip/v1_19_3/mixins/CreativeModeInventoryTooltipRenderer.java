package me.noci.advancedtooltip.v1_19_3.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import me.noci.advancedtooltip.core.utils.FoodIcons;
import me.noci.advancedtooltip.v1_19_3.utils.IconComponentWrapper;
import me.noci.advancedtooltip.v1_19_3.utils.ItemCast;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryTooltipRenderer {

    @Inject(method = "renderTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen;renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/List;Ljava/util/Optional;II)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void renderTooltip(PoseStack poseStack, ItemStack itemStack, int x, int y, CallbackInfo ci, boolean $$4, boolean $$5, boolean $$6, TooltipFlag.Default $$7, TooltipFlag $$8, List<Component> $$9, List<Component> finalComponents) {
        if (!AdvancedTooltipAddon.enabled()) {
            return;
        }

        if (itemStack.isEdible()) {
            net.labymod.api.client.world.item.ItemStack labyItemStack = ItemCast.toLabyItemStack(itemStack);
            List<IconComponentWrapper> icons = FoodIcons.getIcons(labyItemStack, IconComponentWrapper::new, IconComponentWrapper.class);

            finalComponents.addAll(icons);
        }
    }

}
