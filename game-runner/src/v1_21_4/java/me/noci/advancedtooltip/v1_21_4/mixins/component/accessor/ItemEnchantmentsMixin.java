package me.noci.advancedtooltip.v1_21_4.mixins.component.accessor;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import me.noci.advancedtooltip.v1_21_4.components.accessor.ItemEnchantmentsAccessor;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(ItemEnchantments.class)
public class ItemEnchantmentsMixin implements ItemEnchantmentsAccessor {

    @Final @Shadow boolean showInTooltip;

    @Final @Shadow Object2IntOpenHashMap<Holder<Enchantment>> enchantments;

    @Override
    public boolean isShownInTooltip() {
        return showInTooltip;
    }

    @Override
    public Set<Holder<Enchantment>> enchantments() {
        return enchantments.keySet();
    }

}
