package me.noci.advancedtooltip.v1_21.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.component.WrittenBookContent;

import java.util.Objects;

public class WrittenBookContentComponentRenderer implements ComponentRenderer<WrittenBookContent> {
    @Override
    public ComponentPrinter parse(WrittenBookContent writtenBookContent) {
        var pages = writtenBookContent.getPages(false).stream().map(page -> page.toString().replaceAll("\n", "<br>")).toList();

        var titleComponent = ComponentPrinter.value("title", writtenBookContent.title().get(false));
        var authorComponent = ComponentPrinter.value("author", writtenBookContent.author());
        var generationComponent = ComponentPrinter.value("generation", writtenBookContent.generation());
        var resolvedComponent = ComponentPrinter.value("resolved", writtenBookContent.resolved());
        var pagesComponent = ComponentPrinter.expandableList("pages", pages).handler(Objects::toString);

        return ComponentPrinter.object(titleComponent, authorComponent, generationComponent, resolvedComponent, pagesComponent);
    }
}
