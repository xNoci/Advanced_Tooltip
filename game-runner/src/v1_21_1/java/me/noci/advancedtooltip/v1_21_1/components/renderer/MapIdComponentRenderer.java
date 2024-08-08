package me.noci.advancedtooltip.v1_21_1.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.level.saveddata.maps.MapId;

public class MapIdComponentRenderer implements ComponentRenderer<MapId> {
    @Override
    public ComponentPrinter parse(MapId value) {
        return ComponentPrinter.value("map_id", value.id());
    }
}
