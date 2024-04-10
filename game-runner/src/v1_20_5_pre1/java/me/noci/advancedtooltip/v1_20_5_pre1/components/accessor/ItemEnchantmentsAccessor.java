package me.noci.advancedtooltip.v1_20_5_pre1.components.accessor;

import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;

public interface ItemEnchantmentsAccessor {
    boolean isShownInTooltip();

    List<Enchantment> enchantments();
}
