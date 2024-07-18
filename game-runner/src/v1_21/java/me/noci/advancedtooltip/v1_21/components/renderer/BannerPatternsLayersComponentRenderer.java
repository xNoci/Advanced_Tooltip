package me.noci.advancedtooltip.v1_21.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

import java.util.ArrayList;
import java.util.List;

public class BannerPatternsLayersComponentRenderer implements ComponentRenderer<BannerPatternLayers> {
    @Override
    public ComponentPrinter parse(BannerPatternLayers bannerPatternLayers) {
        List<ComponentPrinter> bannerLayerComponents = new ArrayList<>();

        for (BannerPatternLayers.Layer layer : bannerPatternLayers.layers()) {
            ComponentPrinter pattern = ComponentPrinter.value("pattern", layer.pattern().getRegisteredName());
            ComponentPrinter dyeColor = ComponentPrinter.value("dye_color", layer.color().getName());

            bannerLayerComponents.add(ComponentPrinter.object(pattern, dyeColor));
        }

        return ComponentPrinter.expandableList("layers", bannerLayerComponents).handler(ComponentPrinter::print);
    }
}
