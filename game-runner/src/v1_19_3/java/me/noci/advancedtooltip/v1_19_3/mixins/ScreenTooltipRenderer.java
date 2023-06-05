package me.noci.advancedtooltip.v1_19_3.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import me.noci.advancedtooltip.core.utils.FoodIcons;
import me.noci.advancedtooltip.v1_19_3.utils.ItemCast;
import me.noci.advancedtooltip.v1_19_3.utils.VersionedClientIconComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Mixin(Screen.class)
public abstract class ScreenTooltipRenderer {

    @Shadow
    public abstract List<Component> getTooltipFromItem(ItemStack $$0);

    @Shadow
    public abstract void renderTooltip(PoseStack $$0, List<Component> $$1, Optional<TooltipComponent> $$2, int $$3, int $$4);

    @Inject(method = "renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/item/ItemStack;II)V", at = @At("HEAD"), cancellable = true)
    public void renderItemStackToolTipp(PoseStack poseStack, ItemStack itemStack, int x, int y, CallbackInfo ci) {
        if (!AdvancedTooltipAddon.enabled() || !itemStack.isEdible()) {
            return;
        }

        net.labymod.api.client.world.item.ItemStack labyItemStack = ItemCast.toLabyItemStack(itemStack);
        List<VersionedClientIconComponent> icons = FoodIcons.getIcons(labyItemStack, VersionedClientIconComponent::new, VersionedClientIconComponent.class);

        List<Component> components = getTooltipFromItem(itemStack);
        components.addAll(icons);

        renderTooltip(poseStack, components, itemStack.getTooltipImage(), x, y);

        ci.cancel();
    }

    @ModifyVariable(method = "renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/List;Ljava/util/Optional;II)V", at = @At(value = "STORE"), index = 6)
    public List<ClientTooltipComponent> alterComponentList(List<ClientTooltipComponent> original, PoseStack poseStack, List<Component> components) {
        if (!AdvancedTooltipAddon.enabled()) return original;

        return components.stream().map(component -> {
            if (component instanceof VersionedClientIconComponent iconComponent) {
                return iconComponent;
            }
            return new ClientTextTooltip(component.getVisualOrderText());
        }).collect(Collectors.toList());
    }

}
