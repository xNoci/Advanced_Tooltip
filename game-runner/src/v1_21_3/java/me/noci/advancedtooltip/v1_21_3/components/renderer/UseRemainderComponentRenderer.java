package me.noci.advancedtooltip.v1_21_3.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.component.UseRemainder;

public class UseRemainderComponentRenderer implements ComponentRenderer<UseRemainder> {
    @Override
    public ComponentPrinter parse(UseRemainder value) {
        return ComponentPrinter.object(
                ComponentPrinter.value("item", value.convertInto().getItem()),
                ComponentPrinter.value("count", value.convertInto().getCount())
        ).inline();
    }
}
