package me.noci.advancedtooltip.v24w19b.components.accessor;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface ItemContainerContentsAccessor {
    List<Slot> slots();

    record Slot(int index, ItemStack item) {
    }
}