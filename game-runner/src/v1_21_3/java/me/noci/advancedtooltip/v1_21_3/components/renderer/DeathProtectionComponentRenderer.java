package me.noci.advancedtooltip.v1_21_3.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.component.DeathProtection;

import java.util.List;

public class DeathProtectionComponentRenderer implements ComponentRenderer<DeathProtection> {
    @Override
    public ComponentPrinter parse(DeathProtection value) {
        List<ComponentPrinter> effects = ConsumableComponentRenderer.getConsumeEffectPrinters(value.deathEffects());
        return ComponentPrinter.list("custom_effects", effects).handler(ComponentPrinter::print);
    }
}
