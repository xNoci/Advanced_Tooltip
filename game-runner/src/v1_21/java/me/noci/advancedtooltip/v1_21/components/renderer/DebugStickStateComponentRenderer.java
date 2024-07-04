package me.noci.advancedtooltip.v1_21.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.component.DebugStickState;

public class DebugStickStateComponentRenderer implements ComponentRenderer<DebugStickState> {
    @Override
    public ComponentPrinter parse(DebugStickState debugStickState) {
        var entries = debugStickState.properties().entrySet().stream().map(propertyEntry -> {
            String blockKey = propertyEntry.getKey().getRegisteredName();
            String propertyName = propertyEntry.getValue().getName();
            String propertyValues = propertyEntry.getValue().getPossibleValues().toString();
            return ComponentPrinter.text("'%s'=%s %s".formatted(blockKey, propertyName, propertyValues));
        }).toList();

        return ComponentPrinter.expandableObject(entries);
    }
}
