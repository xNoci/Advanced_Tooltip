package me.noci.advancedtooltip.v1_21_3.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.enchantment.Repairable;

public class RepairableComponentRenderer implements ComponentRenderer<Repairable> {
    @Override
    public ComponentPrinter parse(Repairable value) {
        return ComponentPrinter.list("items", value.items()).handler(itemValue -> "'%s'".formatted(itemValue.getRegisteredName()));
    }
}
