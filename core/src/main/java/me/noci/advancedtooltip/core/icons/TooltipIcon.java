package me.noci.advancedtooltip.core.icons;

import me.noci.advancedtooltip.core.TooltipAddon;
import me.noci.advancedtooltip.core.config.IconSubSetting;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.render.matrix.Stack;

import java.util.List;

public enum TooltipIcon {

    FULL_FOOD(IconType.FOOD, IconLocation.FOOD_FULL),
    HALF_FOOD(IconType.FOOD, IconLocation.FOOD_HALF),
    FULL_SATURATION(IconType.FOOD, IconLocation.SATURATION_FULL),
    HALF_SATURATION(IconType.FOOD, IconLocation.SATURATION_HALF),
    FULL_ARMOR(IconType.ARMOR, IconLocation.ARMOR_FULL),
    HALF_ARMOR(IconType.ARMOR, IconLocation.ARMOR_HALF);

    private final IconType iconType;
    private final Icon icon;

    TooltipIcon(IconType iconType, IconLocation location) {
        this.iconType = iconType;
        this.icon = location.icon();
    }

    public void draw(Stack stack, int x, int y, int size) {
        icon.render(stack, x, y, size);
    }

    public int getSize() {
        IconSubSetting iconSubSetting = TooltipAddon.get().configuration().iconSubSetting();
        return switch (iconType) {
            case FOOD -> iconSubSetting.foodSize();
            case ARMOR -> iconSubSetting.armorSize();
        };
    }

    public int getSpacing() {
        IconSubSetting iconSubSetting = TooltipAddon.get().configuration().iconSubSetting();
        return switch (iconType) {
            case FOOD -> iconSubSetting.foodSpacing();
            case ARMOR -> iconSubSetting.armorSpacing();
        };
    }

    public static void drawRow(List<TooltipIcon> icons, Stack stack, int x, int y) {
        int cx = x;
        for (TooltipIcon icon : icons) {
            int size = icon.getSize();
            int spacing = icon.getSpacing();

            icon.draw(stack, cx, y, size);
            cx += size + spacing;
        }
    }

    public static int getSize(List<TooltipIcon> icons) {
        int size = 0;
        for (TooltipIcon icon : icons) {
            int temp = icon.getSize();
            if (temp > size) size = temp;
        }
        return size;
    }

    public static int getWidth(List<TooltipIcon> icons) {
        int width = 0;
        for (TooltipIcon icon : icons) {
            int size = icon.getSize();
            int spacing = icon.getSpacing();

            width += size + spacing;
        }
        return width;
    }

    protected enum IconType {
        FOOD,
        ARMOR;
    }

}
