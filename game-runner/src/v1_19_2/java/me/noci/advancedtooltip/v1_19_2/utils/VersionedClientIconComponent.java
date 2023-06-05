package me.noci.advancedtooltip.v1_19_2.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import me.noci.advancedtooltip.core.utils.ClientIconComponent;
import me.noci.advancedtooltip.core.utils.FoodIcons;
import net.labymod.api.client.render.matrix.Stack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
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
    public void renderImage(Font font, int x, int y, PoseStack poseStack, ItemRenderer itemRenderer, int unknown) {
        Stack stack = Stack.create(poseStack);
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
