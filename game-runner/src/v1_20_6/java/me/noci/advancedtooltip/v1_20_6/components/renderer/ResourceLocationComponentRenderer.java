package me.noci.advancedtooltip.v1_20_6.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.resources.ResourceLocation;

public class ResourceLocationComponentRenderer implements ComponentRenderer<ResourceLocation> {
    @Override
    public ComponentPrinter parse(ResourceLocation value) {
        return ComponentPrinter.value("resource_location", value.toString());
    }
}
