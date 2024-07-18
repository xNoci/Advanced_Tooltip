package me.noci.advancedtooltip.v1_20_5.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import me.noci.advancedtooltip.v1_20_5.components.accessor.ItemEnchantmentsAccessor;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class ItemEnchantmentsComponentRenderer implements ComponentRenderer<ItemEnchantments> {
    @Override
    public ComponentPrinter parse(ItemEnchantments value) {
        ItemEnchantmentsAccessor accessor = (ItemEnchantmentsAccessor) value;
        ComponentPrinter tooltipComponent = ComponentPrinter.value("show_in_tooltip", accessor.isShownInTooltip());

        if (value.isEmpty()) {
            return tooltipComponent;
        }

        ComponentPrinter enchantments = ComponentPrinter.list("enchantments", accessor.enchantments())
                .handler(enchantment -> {
                    String enchantmentKey = Util.getRegisteredName(BuiltInRegistries.ENCHANTMENT, enchantment);
                    int level = value.getLevel(enchantment);
                    return "'%s':%s".formatted(enchantmentKey, level);
                });

        return ComponentPrinter.object(tooltipComponent, enchantments);
    }
}
