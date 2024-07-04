package me.noci.advancedtooltip.v1_20_6.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

public class BannerPatternsLayersComponentRenderer implements ComponentRenderer<BannerPatternLayers> {
    @Override
    public ComponentPrinter parse(BannerPatternLayers bannerPatternLayers) {
        var bannerLayers = bannerPatternLayers.layers().stream().map(layer -> {
            var patternComponent = ComponentPrinter.value("pattern", layer.pattern().getRegisteredName());
            var dyeColorComponent = ComponentPrinter.value("dye_color", layer.color().getName());
            return ComponentPrinter.object(patternComponent, dyeColorComponent);
        }).toList();

        return ComponentPrinter.expandableList("layers", bannerLayers).handler(ComponentPrinter::print);
    }
}
