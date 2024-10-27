package me.noci.advancedtooltip.v1_21_3.mixins;

import me.noci.advancedtooltip.core.TooltipAddon;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {

    @Redirect(
            method = "getTooltipLines",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z",
                    ordinal = 0
            )
    )
    private boolean ignoreHideTooltips(ItemStack instance, DataComponentType<?> dataComponentType) {
        if (!TooltipAddon.enabled() || !TooltipAddon.get().configuration().ignoreHideTooltip()) {
            return instance.has(dataComponentType);
        }

        return false;
    }

    @Redirect(
            method = "getTooltipLines",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;isDamaged()Z"
            )
    )
    private boolean disableDurabilityTooltip(ItemStack instance) {
        if (!TooltipAddon.enabled()) {
            return instance.isDamaged();
        }

        return !TooltipAddon.get().configuration().itemDurability().enabled() && instance.isDamaged();
    }

}
