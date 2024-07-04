package me.noci.advancedtooltip.v1_21.components.renderer;

import com.google.common.collect.Lists;
import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import me.noci.advancedtooltip.core.utils.StringUtils;
import me.noci.advancedtooltip.v1_21.utils.PotionEffectUtils;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.List;
import java.util.Optional;

public class PotionContentsComponentRenderer implements ComponentRenderer<PotionContents> {
    @Override
    public ComponentPrinter parse(PotionContents potionContents) {
        Optional<Holder<Potion>> potion = potionContents.potion();
        Optional<Integer> customColor = potionContents.customColor();
        List<MobEffectInstance> customEffects = potionContents.customEffects();

        Optional<ComponentPrinter> customColorComponent = customColor.map(color -> ComponentPrinter.value("custom_color", StringUtils.toHexString(color)));
        ComponentPrinter customEffectListComponent = ComponentPrinter.list("custom_effects", customEffects).handler(PotionEffectUtils::asString);
        Optional<ComponentPrinter> potionComponent = potion.map(potionHolder -> {
            var nameComponent = ComponentPrinter.value("fullName", Potion.getName(potion, ""));
            var listComponent = ComponentPrinter.list("effects", potionHolder.value().getEffects()).handler(PotionEffectUtils::asString);
            return ComponentPrinter.object("potion", nameComponent, listComponent);
        });

        List<ComponentPrinter> components = Lists.newArrayList();
        customColorComponent.ifPresent(components::add);
        potionComponent.ifPresent(components::add);
        components.add(customEffectListComponent);

        return ComponentPrinter.object(components);
    }
}
