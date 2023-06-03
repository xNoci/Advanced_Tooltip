package me.noci.advancedtooltip.v1_19_4.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import me.noci.advancedtooltip.v1_19_4.util.IconComponent;
import me.noci.advancedtooltip.v1_19_4.util.ItemFoodData;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Mixin(Screen.class)
public abstract class ScreenTooltipRenderer {

    private Screen This() {
        return ((Screen) (Object) this);
    }

    @Inject(at = @At("HEAD"), method = "renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/item/ItemStack;II)V", cancellable = true)
    public void renderItemStackToolTipp(PoseStack poseStack, ItemStack itemStack, int x, int y, CallbackInfo ci) {
        if (!AdvancedTooltipAddon.enabled()) {
            return;
        }

        if (itemStack.isEdible()) {
            List<Component> components = This().getTooltipFromItem(itemStack);
            components.addAll(ItemFoodData.getIconFoodData(itemStack));
            This().renderTooltip(poseStack, components, itemStack.getTooltipImage(), x, y);
            ci.cancel();
        }
    }

    @Inject(method = "renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/List;Ljava/util/Optional;II)V", at = @At("HEAD"), cancellable = true)
    public void convertIconComponent(PoseStack poseStack, List<Component> components, Optional<TooltipComponent> tooltipComponent, int x, int y, CallbackInfo ci) {
        if (!AdvancedTooltipAddon.enabled()) {
            return;
        }

        List<ClientTooltipComponent> clientTooltipComponents = components.stream().map(component -> {
            if (component instanceof IconComponent iconComponent) {
                return iconComponent.getClientIconComponent();
            }
            return new ClientTextTooltip(component.getVisualOrderText());
        }).collect(Collectors.toList());

        tooltipComponent.ifPresent(component -> clientTooltipComponents.add(1, ClientTooltipComponent.create(component)));

        renderTooltipInternal(poseStack, clientTooltipComponents, x, y, DefaultTooltipPositioner.INSTANCE);
        ci.cancel();
    }

    @Shadow
    protected abstract void renderTooltipInternal(PoseStack poseStack, List<ClientTooltipComponent> ctComponents, int x, int y, ClientTooltipPositioner ctPositioner);

}
