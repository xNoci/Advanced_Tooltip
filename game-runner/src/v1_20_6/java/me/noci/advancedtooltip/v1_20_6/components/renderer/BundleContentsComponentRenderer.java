package me.noci.advancedtooltip.v1_20_6.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.component.BundleContents;

public class BundleContentsComponentRenderer implements ComponentRenderer<BundleContents> {
    @Override
    public ComponentPrinter parse(BundleContents bundleContents) {
        var weightComponent = ComponentPrinter.value("weight", bundleContents.weight());

        if (bundleContents.isEmpty()) {
            return weightComponent;
        }

        var itemsComponent = ComponentPrinter.expandableList("items", bundleContents.items()).handler(itemStack -> {
            String itemKey = Util.getRegisteredName(BuiltInRegistries.ITEM, itemStack.getItem());
            int amount = itemStack.getCount();
            return "'%s':%s".formatted(itemKey, amount);
        });

        return ComponentPrinter.object(weightComponent, itemsComponent);
    }
}
