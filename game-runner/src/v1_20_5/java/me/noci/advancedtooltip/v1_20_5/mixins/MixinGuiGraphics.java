package me.noci.advancedtooltip.v1_20_5.mixins;

import me.noci.advancedtooltip.core.TooltipAddon;
import me.noci.advancedtooltip.v1_20_5.utils.VersionedClientIconComponent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(GuiGraphics.class)
public class MixinGuiGraphics {

    @ModifyVariable(
            method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V",
            at = @At(
                    value = "STORE"
            ),
            index = 6
    )
    private List<ClientTooltipComponent> alterComponentList(List<ClientTooltipComponent> original, Font font, List<Component> components) {
        if (!TooltipAddon.enabled()) return original;

        return components.stream().map(component -> {
            if (component instanceof VersionedClientIconComponent iconComponent) {
                return iconComponent;
            }
            return new ClientTextTooltip(component.getVisualOrderText());
        }).collect(Collectors.toCollection(ArrayList::new));
    }

}
