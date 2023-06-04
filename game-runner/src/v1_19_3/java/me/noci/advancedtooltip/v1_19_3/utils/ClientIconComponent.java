package me.noci.advancedtooltip.v1_19_3.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import me.noci.advancedtooltip.core.utils.IconData;
import net.labymod.api.client.render.matrix.Stack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;

import java.util.List;

public class ClientIconComponent implements ClientTooltipComponent {

    private final List<IconData> icons;
    private final int iconSize;
    private final int iconSpacing;
    private final int height;
    private final int width;

    public ClientIconComponent(List<IconData> icons) {
        iconSize = AdvancedTooltipAddon.getInstance().configuration().iconSize().get();
        iconSpacing = AdvancedTooltipAddon.getInstance().configuration().iconSpacing().get();

        int height = 0;
        int width = 0;

        for (IconData icon : icons) {
            width += iconSize + iconSpacing;

            int iconHeight = iconSize;
            if (iconHeight > height) {
                height = iconHeight;
            }
        }

        this.icons = icons;
        this.height = height;
        this.width = width;
    }

    @Override
    public int getHeight() {
        return this.height + 2;
    }

    @Override
    public int getWidth(Font font) {
        return this.width;
    }

    @Override
    public void renderImage(Font font, int x, int y, PoseStack poseStack, ItemRenderer itemRenderer, int unknown) {
        Stack stack = Stack.create(poseStack);

        int cx = x;
        for (IconData icon : icons) {
            icon.draw(stack, cx, y, iconSize);
            cx += iconSize + iconSpacing;
        }
    }
}

