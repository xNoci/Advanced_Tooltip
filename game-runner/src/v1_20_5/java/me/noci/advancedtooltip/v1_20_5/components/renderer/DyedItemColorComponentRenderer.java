package me.noci.advancedtooltip.v1_20_5.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import me.noci.advancedtooltip.core.utils.StringUtils;
import net.minecraft.world.item.component.DyedItemColor;

public class DyedItemColorComponentRenderer implements ComponentRenderer<DyedItemColor> {
    @Override
    public ComponentPrinter parse(DyedItemColor value) {
        var tooltipComponent = ComponentPrinter.value("show_in_tooltip", value.showInTooltip());
        var rgbComponent = ComponentPrinter.value("rgb", "0x%s".formatted(StringUtils.toHexString(value.rgb())));
        return ComponentPrinter.object(tooltipComponent, rgbComponent);
    }

}
