package me.noci.advancedtooltip.v1_20_5.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.component.BlockItemStateProperties;

public class BlockItemStatePropertiesComponentRenderer implements ComponentRenderer<BlockItemStateProperties> {
    @Override
    public ComponentPrinter parse(BlockItemStateProperties value) {
        return ComponentPrinter.map("properties", value.properties()).handler(key -> key, ComponentPrinter::text);
    }
}
