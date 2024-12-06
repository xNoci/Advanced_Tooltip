package me.noci.advancedtooltip.v1_21_4.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.labymod.api.util.I18n;
import net.minecraft.world.LockCode;

public class LockCodeComponentRenderer implements ComponentRenderer<LockCode> {
    @Override
    public ComponentPrinter parse(LockCode value) {
        return ComponentPrinter.text("locked");
    }
}
