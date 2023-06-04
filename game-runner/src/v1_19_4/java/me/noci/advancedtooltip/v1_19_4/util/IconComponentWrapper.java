package me.noci.advancedtooltip.v1_19_4.util;

import me.noci.advancedtooltip.core.utils.FoodIcons;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class IconComponentWrapper implements Component {

    private final List<FoodIcons> iconData;

    public IconComponentWrapper(List<FoodIcons> iconData) {
        this.iconData = iconData;
    }

    public VersionedClientIconComponent getClientIconComponent() {
        return new VersionedClientIconComponent(iconData);
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
