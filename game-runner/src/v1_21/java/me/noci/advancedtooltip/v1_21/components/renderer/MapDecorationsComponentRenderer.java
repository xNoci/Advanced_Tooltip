package me.noci.advancedtooltip.v1_21.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import me.noci.advancedtooltip.core.utils.StringUtils;
import net.minecraft.world.item.component.MapDecorations;

public class MapDecorationsComponentRenderer implements ComponentRenderer<MapDecorations> {
    @Override
    public ComponentPrinter parse(MapDecorations decorations) {
        return ComponentPrinter.expandableMap("decorations", decorations.decorations())
                .handler(key -> key,
                        decoration -> {
                            var decorationType = decoration.type().value();
                            var typeComponent = ComponentPrinter.expandableObject("type",
                                    ComponentPrinter.value("assetId", decorationType.assetId().toString()),
                                    ComponentPrinter.value("show_on_item_frame", decorationType.showOnItemFrame()),
                                    ComponentPrinter.value("map_color", StringUtils.toHexString(decorationType.mapColor())),
                                    ComponentPrinter.value("exploration_map_element", decorationType.explorationMapElement()),
                                    ComponentPrinter.value("track_count", decorationType.trackCount())
                            );
                            var xComponent = ComponentPrinter.value("x", decoration.x());
                            var zComponent = ComponentPrinter.value("y", decoration.z());
                            var rotationComponent = ComponentPrinter.value("rotation", decoration.rotation());
                            return ComponentPrinter.object(typeComponent, xComponent, zComponent, rotationComponent);
                        });
    }
}
