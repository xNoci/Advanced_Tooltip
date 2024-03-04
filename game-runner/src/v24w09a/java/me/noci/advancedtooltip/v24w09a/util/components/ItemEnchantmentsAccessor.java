package me.noci.advancedtooltip.v24w09a.util.components;

import net.minecraft.world.item.enchantment.Enchantment;

public interface ItemEnchantmentsAccessor {
    boolean isShownInTooltip();

    Enchantment[] enchantments();
}
