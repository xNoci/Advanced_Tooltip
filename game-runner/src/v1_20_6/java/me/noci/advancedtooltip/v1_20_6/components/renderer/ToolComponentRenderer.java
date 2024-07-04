package me.noci.advancedtooltip.v1_20_6.components.renderer;

import com.google.common.collect.Lists;
import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.component.Tool;

import java.util.List;

public class ToolComponentRenderer implements ComponentRenderer<Tool> {
    @Override
    public ComponentPrinter parse(Tool tool) {
        var rulesComponents = tool.rules().stream().map(rule -> {
            List<ComponentPrinter> ruleComponents = Lists.newArrayList();
            ruleComponents.add(ComponentPrinter.expandableList("blocks", rule.blocks().stream().map(Holder::getRegisteredName).toList()));
            rule.speed().ifPresent(speed -> ruleComponents.add(ComponentPrinter.value("speed", speed)));
            rule.correctForDrops().ifPresent(correctForDrops -> ruleComponents.add(ComponentPrinter.value("correct_for_drops", correctForDrops)));
            return ComponentPrinter.object(ruleComponents);
        }).toList();

        return ComponentPrinter.object(
                        ComponentPrinter.value("mining_speed", tool.defaultMiningSpeed()),
                        ComponentPrinter.value("damage_per_block", tool.damagePerBlock()),
                        ComponentPrinter.list("rules", rulesComponents).handler(ComponentPrinter::print)
                )
                .inline(tool.rules().isEmpty());
    }
}
