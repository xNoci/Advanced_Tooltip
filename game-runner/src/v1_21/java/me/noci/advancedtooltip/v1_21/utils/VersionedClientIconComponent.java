package me.noci.advancedtooltip.v1_21.utils;

import me.noci.advancedtooltip.core.icons.IconComponent;
import me.noci.advancedtooltip.core.icons.TooltipIcon;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.render.matrix.VanillaStackAccessor;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class VersionedClientIconComponent extends IconComponent implements ClientTooltipComponent, Component {

    public VersionedClientIconComponent(List<TooltipIcon> icons) {
        super(icons);
    }

    @Override
    public int getHeight() {
        return super.getHeight();
    }

    @Override
    public int getWidth(Font font) {
        return super.getWidth();
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
        Stack stack = ((VanillaStackAccessor) graphics.pose()).stack();
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

