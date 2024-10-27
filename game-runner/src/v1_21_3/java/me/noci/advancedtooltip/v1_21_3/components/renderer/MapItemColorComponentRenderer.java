package me.noci.advancedtooltip.v1_21_3.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import me.noci.advancedtooltip.core.utils.StringUtils;
import net.minecraft.world.item.component.MapItemColor;

public class MapItemColorComponentRenderer implements ComponentRenderer<MapItemColor> {
    @Override
    public ComponentPrinter parse(MapItemColor value) {
        return ComponentPrinter.value("rgb", StringUtils.toHexString(value.rgb()));
    }
}
