package me.noci.advancedtooltip.v1_21_4.components.accessor;

import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Set;

public interface ItemEnchantmentsAccessor {
    boolean isShownInTooltip();

    Set<Holder<Enchantment>> enchantments();

}
