package me.noci.advancedtooltip.core.utils;

import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import net.labymod.api.client.render.matrix.Stack;

import java.util.List;

public abstract class ClientIconComponent {

    private final List<FoodIcon> icons;
    private final int iconSize;
    private final int iconSpacing;
    private final int width;

    public ClientIconComponent(List<FoodIcon> icons) {
        this.icons = icons;
        this.iconSize = AdvancedTooltipAddon.getInstance().configuration().iconSize().get();
        this.iconSpacing = AdvancedTooltipAddon.getInstance().configuration().iconSpacing().get();
        this.width = FoodIcon.getWidth(icons, iconSize, iconSpacing);
    }

    public int getHeight() {
        return this.iconSize + 2;
    }

    public int getWidth() {
        return this.width;
    }

    public void renderIcons(Stack stack, int x, int y) {
        FoodIcon.drawRow(icons, stack, x, y, iconSize, iconSpacing);
    }

}
