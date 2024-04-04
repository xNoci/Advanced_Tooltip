package me.noci.advancedtooltip.v24w14a.components.accessor;

import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;

public interface ItemEnchantmentsAccessor {
    boolean isShownInTooltip();

    List<Enchantment> enchantments();
}
