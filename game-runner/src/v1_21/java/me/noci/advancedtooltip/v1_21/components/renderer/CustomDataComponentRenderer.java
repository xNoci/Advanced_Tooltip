package me.noci.advancedtooltip.v1_21.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.component.CustomData;

public class CustomDataComponentRenderer implements ComponentRenderer<CustomData> {
    @Override
    public ComponentPrinter parse(CustomData data) {
        return ComponentPrinter.nbt(data.copyTag());
    }
}
