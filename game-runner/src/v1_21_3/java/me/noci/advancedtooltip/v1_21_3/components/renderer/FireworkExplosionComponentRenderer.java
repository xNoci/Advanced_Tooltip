package me.noci.advancedtooltip.v1_21_3.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import me.noci.advancedtooltip.core.utils.StringUtils;
import net.minecraft.world.item.component.FireworkExplosion;

public class FireworkExplosionComponentRenderer implements ComponentRenderer<FireworkExplosion> {
    @Override
    public ComponentPrinter parse(FireworkExplosion fireworkExplosion) {
        var shapeComponent = ComponentPrinter.value("shape", fireworkExplosion.shape().name());
        var colorsListComponent = ComponentPrinter.list("colors", fireworkExplosion.colors()).handler(StringUtils::toHexString);
        var fadeColorsListComponent = ComponentPrinter.list("fade_colors", fireworkExplosion.fadeColors()).handler(StringUtils::toHexString);
        var hasTrailComponent = ComponentPrinter.value("has_trail", fireworkExplosion.hasTrail());
        var hasTwinkleComponent = ComponentPrinter.value("has_twinkle", fireworkExplosion.hasTwinkle());

        return ComponentPrinter.object(shapeComponent, colorsListComponent, fadeColorsListComponent, hasTrailComponent, hasTwinkleComponent);
    }
}
