package me.noci.advancedtooltip.core.icons;

import me.noci.advancedtooltip.core.config.icon.IconConfig;
import net.labymod.api.client.render.matrix.Stack;

import java.util.List;

public abstract class ClientIconComponent {

    private final List<TooltipIcon> icons;
    private final int iconSize;
    private final int width;

    private final int iconPaddingTop;
    private final int iconPaddingBottom;
    private final int iconPaddingLeft;
    private final int iconPaddingRight;

    private boolean firstIconComponent = false;
    private boolean lastIconComponent = false;

    public ClientIconComponent(List<TooltipIcon> icons) {
        this.icons = icons;
        this.iconSize = TooltipIcon.maximum(icons, IconConfig::iconSize).orElse(IconConfig.DEFAULT_ICON_SIZE);
        this.width = TooltipIcon.width(icons);

        this.iconPaddingTop = TooltipIcon.maximum(icons, IconConfig::paddingTop).orElse(IconConfig.DEFAULT_PADDING);
        this.iconPaddingBottom = TooltipIcon.maximum(icons, IconConfig::pattingBottom).orElse(IconConfig.DEFAULT_PADDING);
        this.iconPaddingLeft = TooltipIcon.maximum(icons, IconConfig::pattingLeft).orElse(IconConfig.DEFAULT_PADDING);
        this.iconPaddingRight = TooltipIcon.maximum(icons, IconConfig::pattingRight).orElse(IconConfig.DEFAULT_PADDING);
    }

    public int getHeight() {
        return this.iconSize + paddingBottom() + paddingTop() + 2;
    }

    public int getWidth() {
        return this.width + iconPaddingLeft + iconPaddingRight;
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
