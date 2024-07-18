package me.noci.advancedtooltip.v1_21.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.component.WritableBookContent;

public class WritableBookContentComponentRenderer implements ComponentRenderer<WritableBookContent> {
    @Override
    public ComponentPrinter parse(WritableBookContent writableBookContent) {
        var pages = writableBookContent.getPages(false).map(s -> s.replaceAll("\n", "<br>")).toList();
        return ComponentPrinter.expandableList("pages", pages).handler("'%s'"::formatted);
    }
}
