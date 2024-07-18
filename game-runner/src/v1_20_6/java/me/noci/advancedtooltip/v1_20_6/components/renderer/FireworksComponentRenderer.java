package me.noci.advancedtooltip.v1_20_6.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import me.noci.advancedtooltip.core.utils.StringUtils;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;

import java.util.ArrayList;
import java.util.List;

public class FireworksComponentRenderer implements ComponentRenderer<Fireworks> {
    @Override
    public ComponentPrinter parse(Fireworks fireworks) {
        ComponentPrinter flightDuration = ComponentPrinter.value("flight_duration", fireworks.flightDuration());

        List<ComponentPrinter> explosions = new ArrayList<>();

        for (FireworkExplosion explosion : fireworks.explosions()) {
            ComponentPrinter shape = ComponentPrinter.value("shape", explosion.shape().name());
            ComponentPrinter colors = ComponentPrinter.list("colors", explosion.colors()).handler(StringUtils::toHexString);
            ComponentPrinter fadeColors = ComponentPrinter.list("fade_colors", explosion.fadeColors()).handler(StringUtils::toHexString);
            ComponentPrinter hasTrail = ComponentPrinter.value("has_trail", explosion.hasTrail());
            ComponentPrinter hasTwinkle = ComponentPrinter.value("has_twinkle", explosion.hasTwinkle());

            explosions.add(ComponentPrinter.object(shape, colors, fadeColors, hasTrail, hasTwinkle));
        }

        ComponentPrinter explosionList = ComponentPrinter.expandableList("explosions", explosions).handler(ComponentPrinter::print);
        return ComponentPrinter.object(flightDuration, explosionList);
    }
}
