package me.noci.advancedtooltip.v1_20_6.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ResourceLocationListComponentRenderer implements ComponentRenderer<List<ResourceLocation>> {
    @Override
    public ComponentPrinter parse(List<ResourceLocation> recipes) {
        return ComponentPrinter.list("recipes", recipes).handler("'%s'"::formatted);
    }
}
