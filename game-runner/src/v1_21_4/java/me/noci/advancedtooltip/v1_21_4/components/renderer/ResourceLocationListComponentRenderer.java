package me.noci.advancedtooltip.v1_21_4.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;

public class ResourceLocationListComponentRenderer implements ComponentRenderer<List<ResourceKey<Recipe<?>>>> {
    @Override
    public ComponentPrinter parse(List<ResourceKey<Recipe<?>>> recipes) {
        return ComponentPrinter.list("recipes", recipes).handler("'%s'"::formatted);
    }
}
