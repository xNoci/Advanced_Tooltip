package me.noci.advancedtooltip.v1_20_6.components.renderer;

import com.google.common.collect.Lists;
import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import me.noci.advancedtooltip.core.utils.StringUtils;
import net.minecraft.world.item.Rarity;

import java.util.List;

public class RarityComponentRenderer implements ComponentRenderer<Rarity> {
    @Override
    public ComponentPrinter parse(Rarity rarity) {
        List<ComponentPrinter> components = Lists.newArrayList(ComponentPrinter.value("name", rarity.getSerializedName()));

        if (rarity.color().getColor() != null) {
            components.add(ComponentPrinter.value("color", StringUtils.toHexString(rarity.color().getColor())));
        }

        return ComponentPrinter.object("rarity", components).inline();
    }
}
