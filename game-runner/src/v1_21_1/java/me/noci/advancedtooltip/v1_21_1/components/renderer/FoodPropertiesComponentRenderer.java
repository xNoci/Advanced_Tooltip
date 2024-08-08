package me.noci.advancedtooltip.v1_21_1.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import me.noci.advancedtooltip.v1_21_1.utils.PotionEffectUtils;
import net.minecraft.world.food.FoodProperties;

import java.util.ArrayList;
import java.util.List;

public class FoodPropertiesComponentRenderer implements ComponentRenderer<FoodProperties> {
    @Override
    public ComponentPrinter parse(FoodProperties foodProperties) {
        List<ComponentPrinter> effects = new ArrayList<>();

        for (FoodProperties.PossibleEffect possibleEffect : foodProperties.effects()) {

            ComponentPrinter effect = ComponentPrinter.value("effect", PotionEffectUtils.asString(possibleEffect.effect()));
            ComponentPrinter probability = ComponentPrinter.value("probability", possibleEffect.probability());

            effects.add(ComponentPrinter.expandableObject(effect, probability));
        }

        return ComponentPrinter.object(
                ComponentPrinter.value("nutrition", foodProperties.nutrition()),
                ComponentPrinter.value("saturation", foodProperties.saturation()),
                ComponentPrinter.value("can_always_eat", foodProperties.canAlwaysEat()),
                ComponentPrinter.value("eat_seconds", foodProperties.eatSeconds()),
                ComponentPrinter.expandableList("effects", effects).handler(ComponentPrinter::print)
        );
    }
}
