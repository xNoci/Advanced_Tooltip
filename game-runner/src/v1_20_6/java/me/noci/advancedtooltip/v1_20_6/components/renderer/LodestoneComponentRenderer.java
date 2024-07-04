package me.noci.advancedtooltip.v1_20_6.components.renderer;

import com.google.common.collect.Lists;
import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.component.LodestoneTracker;

import java.util.List;

public class LodestoneComponentRenderer implements ComponentRenderer<LodestoneTracker> {
    @Override
    public ComponentPrinter parse(LodestoneTracker lodestoneTracker) {
        List<ComponentPrinter> components = Lists.newArrayList();

        lodestoneTracker.target().ifPresent(globalPos -> {
            var blockPos = globalPos.pos();
            var xComponent = ComponentPrinter.value("x", blockPos.getX());
            var yComponent = ComponentPrinter.value("y", blockPos.getY());
            var zComponent = ComponentPrinter.value("z", blockPos.getZ());

            components.add(ComponentPrinter.object("pos", xComponent, yComponent, zComponent));
            components.add(ComponentPrinter.value("dimension", globalPos.dimension().location().toString()));
        });

        components.add(ComponentPrinter.value("tracked", lodestoneTracker.tracked()));

        return ComponentPrinter.object(components);
    }
}
