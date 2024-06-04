package me.noci.advancedtooltip.v1_12_2.mixins;

import me.noci.advancedtooltip.core.TooltipAddon;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {

    @Redirect(
            method = "getTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;isItemDamaged()Z"
            )
    )
    private boolean disableDurabilityTooltip(ItemStack instance) {
        if (!TooltipAddon.enabled()) {
            return instance.isItemDamaged();
        }

        return !TooltipAddon.get().configuration().itemDurability().enabled() && instance.isItemDamaged();
    }

}
