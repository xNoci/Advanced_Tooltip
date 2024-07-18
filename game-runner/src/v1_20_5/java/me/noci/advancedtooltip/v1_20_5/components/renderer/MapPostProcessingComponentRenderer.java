package me.noci.advancedtooltip.v1_20_5.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.component.MapPostProcessing;

public class MapPostProcessingComponentRenderer implements ComponentRenderer<MapPostProcessing> {
    @Override
    public ComponentPrinter parse(MapPostProcessing value) {
        return ComponentPrinter.value("post_processing", value.name().toLowerCase());
    }
}
