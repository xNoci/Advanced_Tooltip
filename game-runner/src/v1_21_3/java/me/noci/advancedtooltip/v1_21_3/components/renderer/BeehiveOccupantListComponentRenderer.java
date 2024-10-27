package me.noci.advancedtooltip.v1_21_3.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;

import java.util.ArrayList;
import java.util.List;

public class BeehiveOccupantListComponentRenderer implements ComponentRenderer<List<BeehiveBlockEntity.Occupant>> {
    @Override
    public ComponentPrinter parse(List<BeehiveBlockEntity.Occupant> occupants) {
        List<ComponentPrinter> beeComponents = new ArrayList<>();

        for (BeehiveBlockEntity.Occupant occupant : occupants) {
            ComponentPrinter ticksInHive = ComponentPrinter.value("ticks_in_hive", occupant.ticksInHive());
            ComponentPrinter minTicksInHive = ComponentPrinter.value("min_ticks_in_hive", occupant.minTicksInHive());
            ComponentPrinter customData = ComponentPrinter.nbt("entity_data", occupant.entityData().copyTag());

            beeComponents.add(ComponentPrinter.object(ticksInHive, minTicksInHive, customData));
        }

        return ComponentPrinter.list("bees", beeComponents).handler(ComponentPrinter::print);
    }
}
