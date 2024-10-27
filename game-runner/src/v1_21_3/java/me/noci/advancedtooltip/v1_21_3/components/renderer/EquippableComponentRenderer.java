package me.noci.advancedtooltip.v1_21_3.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.equipment.Equippable;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class EquippableComponentRenderer implements ComponentRenderer<Equippable> {
    @Override
    public ComponentPrinter parse(Equippable value) {

        List<ComponentPrinter> components = Lists.newArrayList();

        components.add(ComponentPrinter.value("slot", value.slot()));
        components.add(ComponentPrinter.value("sound", value.equipSound().getRegisteredName()));
        value.model().ifPresent(model -> components.add(ComponentPrinter.value("model", model.toString())));
        value.allowedEntities().ifPresent(allowedEntities -> ComponentPrinter.list("allowed_entities", allowedEntities)
                .handler(Holder::getRegisteredName)
        );
        components.add(ComponentPrinter.value("dispensable", value.dispensable()));
        components.add(ComponentPrinter.value("swappable", value.swappable()));
        components.add(ComponentPrinter.value("damage_on_hurt", value.damageOnHurt()));

        return ComponentPrinter.object(components);
    }
}
