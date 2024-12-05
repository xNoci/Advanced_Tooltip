package me.noci.advancedtooltip.v1_21_4.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import me.noci.advancedtooltip.core.utils.StringUtils;
import net.minecraft.world.item.component.CustomModelData;

public class CustomModelDataComponentRenderer implements ComponentRenderer<CustomModelData> {
    @Override
    public ComponentPrinter parse(CustomModelData customModelData) {
        return ComponentPrinter.object("custom_model_data",
                ComponentPrinter.list("floats", customModelData.floats()).handler(Object::toString),
                ComponentPrinter.list("flags", customModelData.flags()).handler(Object::toString),
                ComponentPrinter.list("strings", customModelData.strings()).handler(Object::toString),
                ComponentPrinter.list("colors", customModelData.colors()).handler(StringUtils::toHexString)
        );
    }
}
