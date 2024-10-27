package me.noci.advancedtooltip.v1_21_3.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.ArrayList;
import java.util.List;

public class ItemAttributeModifiersComponentRenderer implements ComponentRenderer<ItemAttributeModifiers> {
    @Override
    public ComponentPrinter parse(ItemAttributeModifiers attributeModifiers) {
        ComponentPrinter showInTooltip = ComponentPrinter.value("show_in_tooltip", attributeModifiers.showInTooltip());

        List<ComponentPrinter> modifierEntries = new ArrayList<>();

        for (ItemAttributeModifiers.Entry modifier : attributeModifiers.modifiers()) {
            ComponentPrinter attributeKey = ComponentPrinter.value("attribute", modifier.attribute().getRegisteredName());
            ComponentPrinter equipmentSlot = ComponentPrinter.value("equipment_slot", modifier.slot().getSerializedName());

            ComponentPrinter attributeModifier = ComponentPrinter.object("attribute_modifier",
                    ComponentPrinter.value("amount", modifier.modifier().amount()),
                    ComponentPrinter.value("operation", modifier.modifier().operation().getSerializedName()),
                    ComponentPrinter.value("id", modifier.modifier().id().toString())
            );

            modifierEntries.add(ComponentPrinter.object(attributeKey, equipmentSlot, attributeModifier));
        }

        boolean hasModifiers = !attributeModifiers.modifiers().isEmpty();
        ComponentPrinter modifiers = ComponentPrinter.expandableList("modifiers", modifierEntries).handler(ComponentPrinter::print);

        return ComponentPrinter.object(showInTooltip, modifiers).inline(!hasModifiers);
    }
}
