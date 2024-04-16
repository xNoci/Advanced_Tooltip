package me.noci.advancedtooltip.v1_20_5_pre2.components.accessor;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface ItemContainerContentsAccessor {
    List<Slot> slots();

    record Slot(int index, ItemStack item) {
    }
}
