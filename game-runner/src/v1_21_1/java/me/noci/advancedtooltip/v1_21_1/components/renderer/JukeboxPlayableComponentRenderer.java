package me.noci.advancedtooltip.v1_21_1.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.JukeboxPlayable;

public class JukeboxPlayableComponentRenderer implements ComponentRenderer<JukeboxPlayable> {
    @Override
    public ComponentPrinter parse(JukeboxPlayable value) {
        return ComponentPrinter.object(
                ComponentPrinter.value("song", value.song().key().location().toString()),
                ComponentPrinter.value("show_in_tooltip", value.showInTooltip()));
    }
}
