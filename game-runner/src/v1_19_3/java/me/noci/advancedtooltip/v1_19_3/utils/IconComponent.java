package me.noci.advancedtooltip.v1_19_3.utils;

import me.noci.advancedtooltip.core.utils.IconData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class IconComponent implements Component {

    private final List<IconData> iconData;

    public IconComponent(List<IconData> iconData) {
        this.iconData = iconData;
    }

    public ClientIconComponent getClientIconComponent() {
        return new ClientIconComponent(iconData);
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
