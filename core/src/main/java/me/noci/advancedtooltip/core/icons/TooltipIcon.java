package me.noci.advancedtooltip.core.icons;

import lombok.Getter;
import me.noci.advancedtooltip.core.TooltipAddon;
import me.noci.advancedtooltip.core.config.icon.IconConfig;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.render.matrix.Stack;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public enum TooltipIcon {

    FULL_FOOD(TooltipAddon.get().configuration().nutritionIcons(), IconLocation.FOOD_FULL),
    HALF_FOOD(TooltipAddon.get().configuration().nutritionIcons(), IconLocation.FOOD_HALF),
    FULL_SATURATION(TooltipAddon.get().configuration().saturationIcons(), IconLocation.SATURATION_FULL),
    HALF_SATURATION(TooltipAddon.get().configuration().saturationIcons(), IconLocation.SATURATION_HALF),
    FULL_ARMOR(TooltipAddon.get().configuration().armorIcons(), IconLocation.ARMOR_FULL),
    HALF_ARMOR(TooltipAddon.get().configuration().armorIcons(), IconLocation.ARMOR_HALF);

    @Getter
    private final IconConfig iconConfig;
    private final Icon icon;

    TooltipIcon(IconConfig iconConfig, IconLocation location) {
        this.iconConfig = iconConfig;
        this.icon = location.icon();
    }

    public static void drawRow(List<TooltipIcon> icons, Stack stack, int x, int y) {
        int cx = x;
        for (TooltipIcon icon : icons) {
            int size = icon.iconConfig().iconSize();
            int spacing = icon.iconConfig.iconSpacing();

            icon.draw(stack, cx, y, size);
            cx += size + spacing;
        }
    }

    public static Optional<Integer> maximum(List<TooltipIcon> icons, Function<IconConfig, Integer> comparedValue) {
        return icons.stream().map(TooltipIcon::iconConfig).map(comparedValue).max(Integer::compareTo);
    }

    public static int width(List<TooltipIcon> icons) {
        return icons.stream()
                .map(icon -> icon.iconConfig.iconSize() + icon.iconConfig.iconSpacing())
                .mapToInt(Integer::intValue)
                .sum();
    }

    public void draw(Stack stack, int x, int y, int size) {
        icon.render(stack, x, y, size);
    }

}
