package me.noci.advancedtooltip.v1_20_5.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import me.noci.advancedtooltip.v1_20_5.components.accessor.AdventureModePredicateAccessor;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.AdventureModePredicate;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class AdventureModePredicateComponentRenderer implements ComponentRenderer<AdventureModePredicate> {
    @Override
    public ComponentPrinter parse(AdventureModePredicate value) {
        AdventureModePredicateAccessor accessor = (AdventureModePredicateAccessor) value;

        List<ComponentPrinter> blockComponentList = new ArrayList<>();

        for (BlockPredicate predicate : accessor.predicates()) {
            HolderSet<Block> blocks = predicate.blocks().orElse(null);
            if (blocks == null) continue;
            if (blocks.size() == 0) continue;
            ComponentPrinter printer = ComponentPrinter.list("blocks", blocks)
                    .handler(block -> Util.getRegisteredName(BuiltInRegistries.BLOCK, block.value()));
            blockComponentList.add(printer);
        }

        ComponentPrinter tooltipComponent = ComponentPrinter.value("show_in_tooltip", accessor.showInTooltip());
        ComponentPrinter predicateListComponent = ComponentPrinter.expandableList("predicates", blockComponentList).handler(ComponentPrinter::print);
        return ComponentPrinter.object(tooltipComponent, predicateListComponent);
    }
}
