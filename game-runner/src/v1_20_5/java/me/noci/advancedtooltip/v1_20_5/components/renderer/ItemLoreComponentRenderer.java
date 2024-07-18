package me.noci.advancedtooltip.v1_20_5.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.component.ItemLore;

public class ItemLoreComponentRenderer implements ComponentRenderer<ItemLore> {
    @Override
    public ComponentPrinter parse(ItemLore value) {
        return ComponentPrinter.expandableList("lines", value.lines())
                .handler(line -> line.toString().replaceAll("\n", "<br>"));
    }
}
