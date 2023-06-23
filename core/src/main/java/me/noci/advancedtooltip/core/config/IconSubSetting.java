package me.noci.advancedtooltip.core.config;

import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class IconSubSetting extends Config {

    @SliderWidget.SliderSetting(min = 4.0f, max = 16.0f)
    protected final ConfigProperty<Integer> iconSize = new ConfigProperty<>(8);
    @SliderWidget.SliderSetting(min = 0.0f, max = 8.0f)
    protected final ConfigProperty<Integer> iconSpacing = new ConfigProperty<>(2);

}
