package me.noci.advancedtooltip.v1_20_6.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.component.CustomModelData;

public class CustomModelDataComponentRenderer implements ComponentRenderer<CustomModelData> {
    @Override
    public ComponentPrinter parse(CustomModelData customModelData) {
        return ComponentPrinter.value("custom_model_data", customModelData.value());
    }
}
