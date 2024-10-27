package me.noci.advancedtooltip.v1_21_3.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.component.Unbreakable;

public class UnbreakableComponentRenderer implements ComponentRenderer<Unbreakable> {
    @Override
    public ComponentPrinter parse(Unbreakable value) {
        return ComponentPrinter.value("show_in_tooltip", value.showInTooltip());
    }
}
