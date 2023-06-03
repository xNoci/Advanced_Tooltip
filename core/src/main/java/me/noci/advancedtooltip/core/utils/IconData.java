package me.noci.advancedtooltip.core.utils;

import net.labymod.api.Laby;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;

public enum IconData {

    FULL_FOOD(52, 27),
    HALF_FOOD(61, 27),
    FULL_SATURATION(70, 27),
    HALF_SATURATION(79, 27);

    private final Icon icon;

    IconData(int x, int y) {
        ResourceLocation location = Laby.labyAPI().minecraft().textures().iconsTexture();
        icon = Icon.sprite(location, x, y, 9, 9, 256, 256);
    }

    public void draw(Stack stack, int x, int y, int size) {
        icon.render(stack, x, y, size);
    }

}
