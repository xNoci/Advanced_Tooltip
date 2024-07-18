package me.noci.advancedtooltip.v1_21.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;

public class IntegerComponentRenderer implements ComponentRenderer<Integer> {
    @Override
    public ComponentPrinter parse(Integer value) {
        return ComponentPrinter.value("int", value);
    }
}
