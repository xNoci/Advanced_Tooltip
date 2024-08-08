package me.noci.advancedtooltip.v1_21_1.components.accessor;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Set;

public interface ItemEnchantmentsAccessor {
    boolean isShownInTooltip();

    Set<Holder<Enchantment>> enchantments();

    default int getLevel(ItemStack itemStack, ResourceKey<Enchantment> enchantmentResourceKey) {

        for (Holder<Enchantment> enchantment : enchantments()) {
            if (!enchantment.is(enchantmentResourceKey)) continue;
            return itemStack.getEnchantments().getLevel(enchantment);
        }

        return 0;
    }

}
