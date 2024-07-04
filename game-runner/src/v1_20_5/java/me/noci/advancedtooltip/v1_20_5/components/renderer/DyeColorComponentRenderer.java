package me.noci.advancedtooltip.v1_20_5.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.DyeColor;

public class DyeColorComponentRenderer implements ComponentRenderer<DyeColor> {
    @Override
    public ComponentPrinter parse(DyeColor value) {
        return ComponentPrinter.value("dye_color", value.getName());
    }
}
