package me.noci.advancedtooltip.v1_21_1.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Instrument;

public class InstrumentComponentRenderer implements ComponentRenderer<Holder<Instrument>> {
    @Override
    public ComponentPrinter parse(Holder<Instrument> instrumentHolder) {
        return ComponentPrinter.value("instrument", instrumentHolder.getRegisteredName());
    }
}
