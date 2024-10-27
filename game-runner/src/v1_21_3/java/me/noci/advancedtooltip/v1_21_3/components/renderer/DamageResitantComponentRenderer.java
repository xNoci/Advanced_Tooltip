package me.noci.advancedtooltip.v1_21_3.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.component.DamageResistant;

public class DamageResitantComponentRenderer implements ComponentRenderer<DamageResistant> {
    @Override
    public ComponentPrinter parse(DamageResistant value) {
        return ComponentPrinter.value("damage_type", value.types().location().toString());
    }
}
