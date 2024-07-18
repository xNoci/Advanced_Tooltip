package me.noci.advancedtooltip.v1_21.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;

public class BooleanComponentRenderer implements ComponentRenderer<Boolean> {
    @Override
    public ComponentPrinter parse(Boolean value) {
        return ComponentPrinter.value("boolean", value);
    }
}
