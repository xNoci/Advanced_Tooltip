package me.noci.advancedtooltip.v1_20_5.components.renderer;

import com.google.common.collect.Lists;
import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import me.noci.advancedtooltip.v1_20_5.components.accessor.AdventureModePredicateAccessor;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.AdventureModePredicate;

public class AdventureModePredicateComponentRenderer implements ComponentRenderer<AdventureModePredicate> {
    @Override
    public ComponentPrinter parse(AdventureModePredicate value) {
        AdventureModePredicateAccessor accessor = (AdventureModePredicateAccessor) value;

        var tooltipComponent = ComponentPrinter.value("show_in_tooltip", accessor.showInTooltip());
        var blockListComponent = accessor.predicates().stream()
                .filter(blockPredicates -> blockPredicates.blocks().isPresent())
                .map(blockPredicates -> Lists.newArrayList(blockPredicates.blocks().get()))
                .filter(blocks -> !blocks.isEmpty())
                .map(blocks -> ComponentPrinter.list("blocks", blocks).handler(block -> Util.getRegisteredName(BuiltInRegistries.BLOCK, block.value()))).toList();
        var predicateListComponent = ComponentPrinter.expandableList("predicates", blockListComponent).handler(ComponentPrinter::print);
        return ComponentPrinter.object(tooltipComponent, predicateListComponent);
    }
}
