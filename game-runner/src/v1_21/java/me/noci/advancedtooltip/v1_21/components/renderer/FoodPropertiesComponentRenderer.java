package me.noci.advancedtooltip.v1_21.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import me.noci.advancedtooltip.v1_21.utils.PotionEffectUtils;
import net.minecraft.world.food.FoodProperties;

public class FoodPropertiesComponentRenderer implements ComponentRenderer<FoodProperties> {
    @Override
    public ComponentPrinter parse(FoodProperties foodProperties) {
        var effectComponents = foodProperties.effects().stream().map(possibleEffect -> ComponentPrinter.expandableObject(
                ComponentPrinter.value("effect", PotionEffectUtils.asString(possibleEffect.effect())),
                ComponentPrinter.value("probability", possibleEffect.probability())
        )).toList();

        return ComponentPrinter.object(
                ComponentPrinter.value("nutrition", foodProperties.nutrition()),
                ComponentPrinter.value("saturation", foodProperties.saturation()),
                ComponentPrinter.value("can_always_eat", foodProperties.canAlwaysEat()),
                ComponentPrinter.value("eat_seconds", foodProperties.eatSeconds()),
                ComponentPrinter.expandableList("effects", effectComponents).handler(ComponentPrinter::print)
        );
    }
}
