package me.noci.advancedtooltip.v24w19b.components.accessor;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;

public interface ItemEnchantmentsAccessor {
    boolean isShownInTooltip();

    List<Holder<Enchantment>> enchantments();

    default int getLevel(ItemStack itemStack, ResourceKey<Enchantment> enchantmentResourceKey) {
        return enchantments().stream()
                .filter(enchantmentHolder -> enchantmentHolder.is(enchantmentResourceKey))
                .findFirst()
                .map(enchantmentHolder -> itemStack.getEnchantments().getLevel(enchantmentHolder))
                .orElse(0);
    }

}
