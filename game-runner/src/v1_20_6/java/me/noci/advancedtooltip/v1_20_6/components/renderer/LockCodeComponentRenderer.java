package me.noci.advancedtooltip.v1_20_6.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.labymod.api.util.I18n;
import net.minecraft.world.LockCode;

public class LockCodeComponentRenderer implements ComponentRenderer<LockCode> {
    @Override
    public ComponentPrinter parse(LockCode value) {
        if (value.key().isBlank()) {
            return ComponentPrinter.text(I18n.translate("advancedtooltip.components.no_lock_set"));
        }
        return ComponentPrinter.value("lock", value.key());
    }
}
