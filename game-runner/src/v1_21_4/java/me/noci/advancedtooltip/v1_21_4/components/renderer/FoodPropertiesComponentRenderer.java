package me.noci.advancedtooltip.v1_21_4.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.food.FoodProperties;

public class FoodPropertiesComponentRenderer implements ComponentRenderer<FoodProperties> {
    @Override
    public ComponentPrinter parse(FoodProperties foodProperties) {
        return ComponentPrinter.object(
                ComponentPrinter.value("nutrition", foodProperties.nutrition()),
                ComponentPrinter.value("saturation", foodProperties.saturation()),
                ComponentPrinter.value("can_always_eat", foodProperties.canAlwaysEat())
        );
    }
}
