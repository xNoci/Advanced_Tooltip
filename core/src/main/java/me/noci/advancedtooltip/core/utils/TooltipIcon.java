package me.noci.advancedtooltip.core.utils;

import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import me.noci.advancedtooltip.core.config.IconSubSetting;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;

import java.util.List;

public enum TooltipIcon {

    FULL_FOOD(IconType.FOOD, 52, 27, 8),
    HALF_FOOD(IconType.FOOD, 61, 27, 8),
    FULL_SATURATION(IconType.FOOD, 70, 27, 8),
    HALF_SATURATION(IconType.FOOD, 79, 27, 8),
    FULL_ARMOR(IconType.ARMOR, 34, 9, 9),
    HALF_ARMOR(IconType.ARMOR, 25, 9, 9);

    private final IconType iconType;
    private final Icon icon;

    TooltipIcon(IconType iconType, int x, int y, int spritSize) {
        this.iconType = iconType;

        ResourceLocation location = Laby.labyAPI().minecraft().textures().iconsTexture();
        this.icon = Icon.sprite(location, x, y, spritSize, spritSize, 256, 256);
    }

    public void draw(Stack stack, int x, int y, int size) {
        icon.render(stack, x, y, size);
    }

    public static void drawRow(List<TooltipIcon> icons, Stack stack, int x, int y, int iconSize, int iconSpacing) {
        //TODO use getSize and getSpacing instead
        int cx = x;
        for (TooltipIcon icon : icons) {
            icon.draw(stack, cx, y, iconSize);
            cx += iconSize + iconSpacing;
        }
    }

    public static int getSize(List<TooltipIcon> icons) {
        IconSubSetting iconSubSetting = AdvancedTooltipAddon.getInstance().configuration().iconSubSetting();
        int size = 0;
        for (TooltipIcon icon : icons) {
            int temp = switch (icon.iconType) {
                case FOOD -> iconSubSetting.foodSize();
                case ARMOR -> iconSubSetting.armorSize();
            };
            if (temp > size) size = temp;
        }
        return size;
    }

    public static int getSpacing(List<TooltipIcon> icons) {
        IconSubSetting iconSubSetting = AdvancedTooltipAddon.getInstance().configuration().iconSubSetting();
        int spacing = 0;
        for (TooltipIcon icon : icons) {
            int temp = switch (icon.iconType) {
                case FOOD -> iconSubSetting.foodSpacing();
                case ARMOR -> iconSubSetting.armorSpacing();
            };
            if (temp > spacing) spacing = temp;
        }
        return spacing;
    }

    public static int getWidth(List<TooltipIcon> icons, int iconSize, int iconSpacing) {
        //TODO use getSize and getSpacing instead
        int width = 0;
        for (TooltipIcon icon : icons) {
            width += iconSize + iconSpacing;
        }
        return width;
    }

    protected enum IconType {
        FOOD,
        ARMOR;
    }

}
