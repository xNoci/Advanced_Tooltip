package me.noci.advancedtooltip.core.utils;

import net.labymod.api.Laby;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;

import java.util.List;

public enum TooltipIcon {

    FULL_FOOD(52, 27, 8),
    HALF_FOOD(61, 27, 8),
    FULL_SATURATION(70, 27, 8),
    HALF_SATURATION(79, 27, 8),
    FULL_ARMOR(34, 9, 9),
    HALF_ARMOR(25, 9, 9);

    private final Icon icon;

    TooltipIcon(int x, int y, int spritSize) {
        ResourceLocation location = Laby.labyAPI().minecraft().textures().iconsTexture();
        icon = Icon.sprite(location, x, y, spritSize, spritSize, 256, 256);
    }

    public void draw(Stack stack, int x, int y, int size) {
        icon.render(stack, x, y, size);
    }

    public static void drawRow(List<TooltipIcon> icons, Stack stack, int x, int y, int iconSize, int iconSpacing) {
        int cx = x;
        for (TooltipIcon icon : icons) {
            icon.draw(stack, cx, y, iconSize);
            cx += iconSize + iconSpacing;
        }
    }

    public static int getWidth(List<TooltipIcon> icons, int iconSize, int iconSpacing) {
        int width = 0;
        for (TooltipIcon icon : icons) {
            width += iconSize + iconSpacing;
        }
        return width;
    }

}
