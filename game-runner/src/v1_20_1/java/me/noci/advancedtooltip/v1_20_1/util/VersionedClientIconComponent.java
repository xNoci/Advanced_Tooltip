package me.noci.advancedtooltip.v1_20_1.util;

import me.noci.advancedtooltip.core.utils.ClientIconComponent;
import me.noci.advancedtooltip.core.utils.FoodIcons;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.v1_20_1.client.render.matrix.StackAccessor;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class VersionedClientIconComponent extends ClientIconComponent implements ClientTooltipComponent, Component {

    public VersionedClientIconComponent(List<FoodIcons> icons) {
        super(icons);
    }

    @Override
    public int getWidth(Font font) {
        return getWidth();
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
        Stack stack = ((StackAccessor) graphics.pose()).stack();
        renderIcons(stack, x, y);
    }

    @Override
    public Style getStyle() {
        return null;
    }

    @Override
    public ComponentContents getContents() {
        return null;
    }

    @Override
    public List<Component> getSiblings() {
        return null;
    }

    @Override
    public FormattedCharSequence getVisualOrderText() {
        return null;
    }
}

