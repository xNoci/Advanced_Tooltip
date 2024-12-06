package me.noci.advancedtooltip.v1_21_4.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import me.noci.advancedtooltip.core.utils.StringUtils;
import net.minecraft.world.item.Rarity;

import java.util.ArrayList;
import java.util.List;

public class RarityComponentRenderer implements ComponentRenderer<Rarity> {
    @Override
    public ComponentPrinter parse(Rarity rarity) {
        List<ComponentPrinter> components = new ArrayList<>();

        components.add(ComponentPrinter.value("name", rarity.getSerializedName()));

        Integer color = rarity.color().getColor();
        if (color != null) {
            components.add(ComponentPrinter.value("color", StringUtils.toHexString(color)));
        }

        return ComponentPrinter.object("rarity", components).inline();
    }
}
