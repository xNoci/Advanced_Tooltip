package me.noci.advancedtooltip.v1_21_1.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.component.SeededContainerLoot;

public class SeededContainerLootComponentRenderer implements ComponentRenderer<SeededContainerLoot> {
    @Override
    public ComponentPrinter parse(SeededContainerLoot seededContainerLoot) {
        var lootTableComponent = ComponentPrinter.value("loot_table", seededContainerLoot.lootTable().toString());
        var seedComponent = ComponentPrinter.value("seed", seededContainerLoot.seed());
        return ComponentPrinter.object(lootTableComponent, seedComponent);
    }
}
