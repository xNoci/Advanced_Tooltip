package me.noci.advancedtooltip.v1_19_2.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import me.noci.advancedtooltip.core.utils.FoodIcons;
import me.noci.advancedtooltip.v1_19_2.utils.ItemCast;
import me.noci.advancedtooltip.v1_19_2.utils.VersionedClientIconComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;
import java.util.stream.Collectors;


@Mixin(Screen.class)
public abstract class ScreenTooltipRenderer {

    @ModifyArgs(method = "renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/item/ItemStack;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/List;Ljava/util/Optional;II)V"))
    private void addIconComponents(Args args, PoseStack poseStack, ItemStack itemStack, int x, int y) {
        if (!AdvancedTooltipAddon.enabled() || !itemStack.isEdible()) {
            return;
        }

        net.labymod.api.client.world.item.ItemStack labyItemStack = ItemCast.toLabyItemStack(itemStack);
        List<VersionedClientIconComponent> icons = FoodIcons.getIcons(labyItemStack, VersionedClientIconComponent::new, VersionedClientIconComponent.class);

        ((List<Component>) args.get(1)).addAll(icons);
    }

    @ModifyVariable(method = "renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/List;Ljava/util/Optional;II)V", at = @At(value = "STORE"), index = 6)
    private List<ClientTooltipComponent> alterComponentList(List<ClientTooltipComponent> original, PoseStack poseStack, List<Component> components) {
        if (!AdvancedTooltipAddon.enabled()) return original;

        return components.stream().map(component -> {
            if (component instanceof VersionedClientIconComponent iconComponent) {
                return iconComponent;
            }
            return new ClientTextTooltip(component.getVisualOrderText());
        }).collect(Collectors.toList());
    }
}
