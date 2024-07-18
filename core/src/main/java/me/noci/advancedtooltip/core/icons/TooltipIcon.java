package me.noci.advancedtooltip.core.icons;

import me.noci.advancedtooltip.core.TooltipAddon;
import me.noci.advancedtooltip.core.config.icon.IconConfig;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.render.matrix.Stack;

import java.util.List;
import java.util.function.Function;

public enum TooltipIcon {

    FULL_FOOD(TooltipAddon.get().configuration().nutritionIcons(), IconLocation.FOOD_FULL),
    HALF_FOOD(TooltipAddon.get().configuration().nutritionIcons(), IconLocation.FOOD_HALF),
    FULL_SATURATION(TooltipAddon.get().configuration().saturationIcons(), IconLocation.SATURATION_FULL),
    HALF_SATURATION(TooltipAddon.get().configuration().saturationIcons(), IconLocation.SATURATION_HALF),
    FULL_ARMOR(TooltipAddon.get().configuration().armorIcons(), IconLocation.ARMOR_FULL),
    HALF_ARMOR(TooltipAddon.get().configuration().armorIcons(), IconLocation.ARMOR_HALF);

    private final IconConfig iconConfig;
    private final Icon icon;

    TooltipIcon(IconConfig iconConfig, IconLocation location) {
        this.iconConfig = iconConfig;
        this.icon = location.icon();
    }

    public static void drawRow(List<TooltipIcon> icons, Stack stack, int x, int y) {
        int cx = x;
        for (TooltipIcon icon : icons) {
            int size = icon.iconConfig.iconSize();
            int spacing = icon.iconConfig.iconSpacing();

            icon.draw(stack, cx, y, size);
            cx += size + spacing;
        }
    }

    public static int maximum(List<TooltipIcon> icons, Function<IconConfig, Integer> comparedValue, int defaultValue) {
        int value = -1;

        for (TooltipIcon icon : icons) {
            int iconValue = comparedValue.apply(icon.iconConfig);
            if (iconValue > value) value = iconValue;
        }

        return value > 0 ? value : defaultValue;
    }

    public static int width(List<TooltipIcon> icons) {
        int width = 0;
        for (TooltipIcon icon : icons) {
            width += icon.iconConfig.iconSize() + icon.iconConfig.iconSpacing();
        }

        return width;
    }

    public void draw(Stack stack, int x, int y, int size) {
        icon.render(stack, x, y, size);
    }

}
