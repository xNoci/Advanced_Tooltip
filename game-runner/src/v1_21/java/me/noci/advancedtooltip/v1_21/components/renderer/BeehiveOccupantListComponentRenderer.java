package me.noci.advancedtooltip.v1_21.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;

import java.util.List;

public class BeehiveOccupantListComponentRenderer implements ComponentRenderer<List<BeehiveBlockEntity.Occupant>> {
    @Override
    public ComponentPrinter parse(List<BeehiveBlockEntity.Occupant> occupants) {
        var bees = occupants.stream().map(occupant -> {
            var ticksInHiveComponent = ComponentPrinter.value("ticks_in_hive", occupant.ticksInHive());
            var minTicksInHiveComponent = ComponentPrinter.value("min_ticks_in_hive", occupant.minTicksInHive());
            var customData = ComponentPrinter.nbt("entity_data", occupant.entityData().copyTag());

            return ComponentPrinter.object(ticksInHiveComponent, minTicksInHiveComponent, customData);
        }).toList();

        return ComponentPrinter.list("bees", bees).handler(ComponentPrinter::print);
    }
}
