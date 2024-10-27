package me.noci.advancedtooltip.v1_21_3.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import me.noci.advancedtooltip.v1_21_3.components.accessor.ItemContainerContentsAccessor;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.ArrayList;
import java.util.List;

public class ItemContainerContentsComponentRenderer implements ComponentRenderer<ItemContainerContents> {
    @Override
    public ComponentPrinter parse(ItemContainerContents value) {
        ItemContainerContentsAccessor accessor = cast(value);

        List<ComponentPrinter> slots = new ArrayList<>();

        for (ItemContainerContentsAccessor.Slot slot : accessor.slots()) {
            String slotIndex = Integer.toString(slot.index());
            String itemKey = Util.getRegisteredName(BuiltInRegistries.ITEM, slot.item().getItem());

            slots.add(ComponentPrinter.value(slotIndex, "'" + itemKey + "':" + slot.item().getCount()));
        }

        return ComponentPrinter.expandableObject("slots", slots);
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object object) {
        return (T) object;
    }

}
