package me.noci.advancedtooltip.core.utils;

import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import net.labymod.api.client.render.matrix.Stack;

import java.util.List;

public abstract class ClientIconComponent {

    private final List<TooltipIcon> icons;
    private final int iconSize;
    private final int width;

    private final int iconPaddingTop;
    private final int iconPaddingBottom;
    private final int iconPaddingLeft;

    private boolean firstIconComponent = false;
    private boolean lastIconComponent = false;

    public ClientIconComponent(List<TooltipIcon> icons) {
        this.icons = icons;
        this.iconSize = TooltipIcon.getSize(icons);
        this.width = TooltipIcon.getWidth(icons);

        this.iconPaddingTop = AdvancedTooltipAddon.getInstance().configuration().iconSubSetting().paddingTop();
        this.iconPaddingBottom = AdvancedTooltipAddon.getInstance().configuration().iconSubSetting().pattingBottom();
        this.iconPaddingLeft = AdvancedTooltipAddon.getInstance().configuration().iconSubSetting().pattingLeft();
    }

    public int getHeight() {
        return this.iconSize + paddingBottom() + paddingTop() + 2;
    }

    public int getWidth() {
        return this.width + iconPaddingLeft;
    }

    public void renderIcons(Stack stack, int x, int y) {
        TooltipIcon.drawRow(icons, stack, x + iconPaddingLeft, y + paddingTop());
    }

    public void setFirstComponent() {
        this.firstIconComponent = true;
    }

    public void setLastComponent() {
        this.lastIconComponent = true;
    }

    private int paddingTop() {
        return firstIconComponent ? iconPaddingTop : 0;
    }

    private int paddingBottom() {
        return lastIconComponent ? iconPaddingBottom : 0;
    }

}
