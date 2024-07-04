package me.noci.advancedtooltip.v1_21.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class ItemAttributeModifiersComponentRenderer implements ComponentRenderer<ItemAttributeModifiers> {
    @Override
    public ComponentPrinter parse(ItemAttributeModifiers modifiers) {
        var showInTooltipComponent = ComponentPrinter.value("show_in_tooltip", modifiers.showInTooltip());
        var modifierObjects = modifiers.modifiers().stream().map(entry -> {
            var attributeKeyComponent = ComponentPrinter.value("attribute", entry.attribute().getRegisteredName());
            var equipmentSlotComponent = ComponentPrinter.value("equipment_slot", entry.slot().getSerializedName());

            var attributeModifierComponent = ComponentPrinter.object("attribute_modifier",
                    ComponentPrinter.value("amount", entry.modifier().amount()),
                    ComponentPrinter.value("operation", entry.modifier().operation().getSerializedName()),
                    ComponentPrinter.value("id", entry.modifier().id().toString())
            );

            return ComponentPrinter.object(attributeKeyComponent, equipmentSlotComponent, attributeModifierComponent);
        }).toList();

        boolean hasModifiers = !modifiers.modifiers().isEmpty();
        var modifierListComponent = ComponentPrinter.expandableList("modifiers", modifierObjects).handler(ComponentPrinter::print);

        return ComponentPrinter.object(showInTooltipComponent, modifierListComponent).inline(!hasModifiers);
    }
}
