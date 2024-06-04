package me.noci.advancedtooltip.v1_17_1.mixins;

import me.noci.advancedtooltip.core.TooltipAddon;
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
