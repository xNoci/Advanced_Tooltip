package me.noci.advancedtooltip.v1_20_6.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import me.noci.advancedtooltip.core.utils.StringUtils;
import net.minecraft.world.item.component.Fireworks;

public class FireworksComponentRenderer implements ComponentRenderer<Fireworks> {
    @Override
    public ComponentPrinter parse(Fireworks fireworks) {
        var flightDurationComponent = ComponentPrinter.value("flight_duration", fireworks.flightDuration());

        var explosionsObjectComponentList = fireworks.explosions().stream().map(explosion -> {
                    var shapeComponent = ComponentPrinter.value("shape", explosion.shape().name());
                    var colorsListComponent = ComponentPrinter.list("colors", explosion.colors()).handler(StringUtils::toHexString);
                    var fadeColorsListComponent = ComponentPrinter.list("fade_colors", explosion.fadeColors()).handler(StringUtils::toHexString);
                    var hasTrailComponent = ComponentPrinter.value("has_trail", explosion.hasTrail());
                    var hasTwinkleComponent = ComponentPrinter.value("has_twinkle", explosion.hasTwinkle());
                    return ComponentPrinter.object(shapeComponent, colorsListComponent, fadeColorsListComponent, hasTrailComponent, hasTwinkleComponent);
                }
        ).toList();

        var explosionsListComponent = ComponentPrinter.expandableList("explosions", explosionsObjectComponentList).handler(ComponentPrinter::print);
        return ComponentPrinter.object(flightDurationComponent, explosionsListComponent);
    }
}
