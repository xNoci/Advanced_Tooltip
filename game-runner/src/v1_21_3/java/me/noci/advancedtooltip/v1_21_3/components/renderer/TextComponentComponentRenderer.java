package me.noci.advancedtooltip.v1_21_3.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.network.chat.Component;

public class TextComponentComponentRenderer implements ComponentRenderer<Component> {
    @Override
    public ComponentPrinter parse(Component value) {
        return ComponentPrinter.value("text", value.toString().replaceAll("\n", "<br>"));
    }
}
