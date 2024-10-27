package me.noci.advancedtooltip.v1_21_3.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.component.OminousBottleAmplifier;

public class OminousBottleAmplifierComponentRenderer implements ComponentRenderer<OminousBottleAmplifier> {
    @Override
    public ComponentPrinter parse(OminousBottleAmplifier ominousBottleAmplifier) {
        return ComponentPrinter.value("amplifier", ominousBottleAmplifier.value());
    }
}