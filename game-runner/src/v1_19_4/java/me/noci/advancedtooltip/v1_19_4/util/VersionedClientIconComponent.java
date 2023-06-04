package me.noci.advancedtooltip.v1_19_4.util;

import com.mojang.blaze3d.vertex.PoseStack;
import me.noci.advancedtooltip.core.utils.ClientIconComponent;
import me.noci.advancedtooltip.core.utils.FoodIcons;
import net.labymod.api.client.render.matrix.Stack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;

import java.util.List;

public class VersionedClientIconComponent extends ClientIconComponent implements ClientTooltipComponent {

    public VersionedClientIconComponent(List<FoodIcons> icons) {
        super(icons);
    }

    @Override
    public int getWidth(Font font) {
        return getWidth();
    }

    @Override
    public void renderImage(Font font, int x, int y, PoseStack poseStack, ItemRenderer itemRenderer) {
        Stack stack = Stack.create(poseStack);
        renderIcons(stack, x, y);
    }

}

