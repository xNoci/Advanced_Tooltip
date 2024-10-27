package me.noci.advancedtooltip.v1_21_3.components.renderer;

import com.google.common.collect.Lists;
import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import me.noci.advancedtooltip.core.utils.StringUtils;
import me.noci.advancedtooltip.v1_21_3.utils.PotionEffectUtils;
import net.minecraft.core.Holder;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.List;
import java.util.Optional;

public class PotionContentsComponentRenderer implements ComponentRenderer<PotionContents> {
    @Override
    public ComponentPrinter parse(PotionContents potionContents) {
        List<ComponentPrinter> components = Lists.newArrayList();

        potionContents.customColor().ifPresent(color -> components.add(ComponentPrinter.value("custom_color", StringUtils.toHexString(color))));

        Optional<Holder<Potion>> potionOptional = potionContents.potion();
        potionOptional.ifPresent(potionHolder -> {
            var nameComponent = ComponentPrinter.value("name", potionHolder.value().name());
            var listComponent = ComponentPrinter.list("effects", potionHolder.value().getEffects()).handler(PotionEffectUtils::asString);
            components.add(ComponentPrinter.object("potion", nameComponent, listComponent));
        });

        potionContents.customName().ifPresent(customName -> components.add(ComponentPrinter.value("custom_name", customName)));
        components.add(ComponentPrinter.list("custom_effects", potionContents.customEffects()).handler(PotionEffectUtils::asString));

        return ComponentPrinter.object(components);
    }
}
