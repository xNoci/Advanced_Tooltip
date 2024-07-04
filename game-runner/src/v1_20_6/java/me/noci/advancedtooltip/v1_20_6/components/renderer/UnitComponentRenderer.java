package me.noci.advancedtooltip.v1_20_6.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.util.Unit;

public class UnitComponentRenderer implements ComponentRenderer<Unit> {

    @Override
    public ComponentPrinter render(Object component, Unit value) {
        return ComponentPrinter.unit(component);
    }

    @Override
    public ComponentPrinter parse(Unit value) {
        return null;
    }
}
