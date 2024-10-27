package me.noci.advancedtooltip.v1_21_3.components.renderer;

import com.google.common.collect.Lists;
import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class ToolComponentRenderer implements ComponentRenderer<Tool> {
    @Override
    public ComponentPrinter parse(Tool tool) {
        List<ComponentPrinter> rules = new ArrayList<>();

        for (Tool.Rule rule : tool.rules()) {
            List<ComponentPrinter> ruleComponents = Lists.newArrayList();

            List<String> blocks = new ArrayList<>();
            for (Holder<Block> block : rule.blocks()) {
                blocks.add(block.getRegisteredName());
            }

            ruleComponents.add(ComponentPrinter.expandableList("blocks", blocks));

            rule.speed().ifPresent(speed -> ruleComponents.add(ComponentPrinter.value("speed", speed)));
            rule.correctForDrops().ifPresent(correctForDrops -> ruleComponents.add(ComponentPrinter.value("correct_for_drops", correctForDrops)));

            rules.add(ComponentPrinter.object(ruleComponents));
        }

        return ComponentPrinter.object(
                        ComponentPrinter.value("mining_speed", tool.defaultMiningSpeed()),
                        ComponentPrinter.value("damage_per_block", tool.damagePerBlock()),
                        ComponentPrinter.list("rules", rules).handler(ComponentPrinter::print)
                )
                .inline(tool.rules().isEmpty());
    }
}
