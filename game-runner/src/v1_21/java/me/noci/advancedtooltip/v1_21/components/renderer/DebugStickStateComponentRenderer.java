package me.noci.advancedtooltip.v1_21.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.component.DebugStickState;

import java.util.ArrayList;
import java.util.List;

public class DebugStickStateComponentRenderer implements ComponentRenderer<DebugStickState> {
    @Override
    public ComponentPrinter parse(DebugStickState debugStickState) {
        List<ComponentPrinter> propertyComponents = new ArrayList<>();

        for (var entry : debugStickState.properties().entrySet()) {
            String blockKey = entry.getKey().getRegisteredName();
            String propertyName = entry.getValue().getName();
            String propertyValues = entry.getValue().getPossibleValues().toString();

            propertyComponents.add(ComponentPrinter.text("'" + blockKey + "'=" + propertyName + " " + propertyValues));
        }

        return ComponentPrinter.expandableObject(propertyComponents);
    }
}
