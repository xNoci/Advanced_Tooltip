package me.noci.advancedtooltip.v1_21_3.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.enchantment.Enchantable;

public class EnchantableComponentRenderer implements ComponentRenderer<Enchantable> {
    @Override
    public ComponentPrinter parse(Enchantable value) {
        return ComponentPrinter.value("value", value.value());
    }
}
