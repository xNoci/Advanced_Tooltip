package me.noci.advancedtooltip.v1_21_4.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.equipment.trim.ArmorTrim;

public class ArmorTrimComponentRenderer implements ComponentRenderer<ArmorTrim> {
    @Override
    public ComponentPrinter parse(ArmorTrim armorTrim) {
        var patternComponent = ComponentPrinter.value("pattern", armorTrim.pattern().getRegisteredName());
        var materialComponent = ComponentPrinter.value("material", armorTrim.material().getRegisteredName());
        var show_in_tooltip = ComponentPrinter.value("show_in_tooltip", armorTrim.showInTooltip());
        return ComponentPrinter.object(patternComponent, materialComponent, show_in_tooltip);
    }
}
