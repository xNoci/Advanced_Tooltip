package me.noci.advancedtooltip.v24w14a.mixins;

import me.noci.advancedtooltip.core.TooltipAddon;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {

    @Shadow
    public abstract DataComponentMap getComponents();

    @Redirect(
            method = "getTooltipLines",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z",
                    ordinal = 0
            )
    )
    private boolean injected(ItemStack instance, DataComponentType<?> dataComponentType) {
        if (!TooltipAddon.enabled() || !TooltipAddon.get().configuration().ignoreHideTooltip()) {
            return this.getComponents().has(dataComponentType);
        }

        return false;
    }

}
