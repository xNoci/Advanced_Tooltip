package me.noci.advancedtooltip.v1_20_6.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.component.SuspiciousStewEffects;

public class SuspiciousStewEffectsComponentRenderer implements ComponentRenderer<SuspiciousStewEffects> {
    @Override
    public ComponentPrinter parse(SuspiciousStewEffects stewEffects) {
        return ComponentPrinter.list("effects", stewEffects.effects())
                .handler(effect -> "'" + effect.effect().getRegisteredName() + "':" + effect.duration());
    }
}
