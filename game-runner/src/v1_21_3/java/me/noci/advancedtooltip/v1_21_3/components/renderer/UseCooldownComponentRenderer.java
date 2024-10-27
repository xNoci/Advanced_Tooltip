package me.noci.advancedtooltip.v1_21_3.components.renderer;

import com.google.common.collect.Lists;
import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.component.UseCooldown;

import java.util.List;

public class UseCooldownComponentRenderer implements ComponentRenderer<UseCooldown> {
    @Override
    public ComponentPrinter parse(UseCooldown value) {
        List<ComponentPrinter> values = Lists.newArrayList(ComponentPrinter.value("seconds", value.seconds()));
        value.cooldownGroup().ifPresent(group -> values.add(ComponentPrinter.value("group", group.toString())));
        return ComponentPrinter.object(values).inline();
    }
}
