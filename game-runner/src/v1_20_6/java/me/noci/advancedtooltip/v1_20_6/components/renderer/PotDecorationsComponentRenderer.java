package me.noci.advancedtooltip.v1_20_6.components.renderer;

import com.google.common.collect.Lists;
import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.PotDecorations;

import java.util.List;

public class PotDecorationsComponentRenderer implements ComponentRenderer<PotDecorations> {
    @Override
    public ComponentPrinter parse(PotDecorations decorations) {
        List<ComponentPrinter> components = Lists.newArrayList();
        decorations.back().ifPresent(item -> components.add(ComponentPrinter.value("back", Util.getRegisteredName(BuiltInRegistries.ITEM, item))));
        decorations.left().ifPresent(item -> components.add(ComponentPrinter.value("left", Util.getRegisteredName(BuiltInRegistries.ITEM, item))));
        decorations.right().ifPresent(item -> components.add(ComponentPrinter.value("right", Util.getRegisteredName(BuiltInRegistries.ITEM, item))));
        decorations.front().ifPresent(item -> components.add(ComponentPrinter.value("front", Util.getRegisteredName(BuiltInRegistries.ITEM, item))));
        return ComponentPrinter.object(components);
    }
}
