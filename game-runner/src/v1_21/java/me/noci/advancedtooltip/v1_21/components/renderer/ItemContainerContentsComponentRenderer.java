package me.noci.advancedtooltip.v1_21.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import me.noci.advancedtooltip.v1_21.components.accessor.ItemContainerContentsAccessor;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.component.ItemContainerContents;

public class ItemContainerContentsComponentRenderer implements ComponentRenderer<ItemContainerContents> {
    @Override
    public ComponentPrinter parse(ItemContainerContents value) {
        ItemContainerContentsAccessor accessor = cast(value);

        var slots = accessor.slots().stream()
                .map(slot -> {
                    String slotIndex = Integer.toString(slot.index());
                    String itemKey = Util.getRegisteredName(BuiltInRegistries.ITEM, slot.item().getItem());
                    return ComponentPrinter.value(slotIndex, "'%s':%s".formatted(itemKey, slot.item().getCount()));
                }).toList();
        return ComponentPrinter.expandableObject("slots", slots);
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object object) {
        return (T) object;
    }

}
