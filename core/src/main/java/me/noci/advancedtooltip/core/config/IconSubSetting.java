package me.noci.advancedtooltip.core.config;

import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;

public class IconSubSetting extends Config {

    @SettingSection("food_icons")
    @SliderWidget.SliderSetting(min = 4.0f, max = 16.0f)
    protected final ConfigProperty<Integer> foodSize = new ConfigProperty<>(8);

    @SliderWidget.SliderSetting(min = 0.0f, max = 8.0f)
    protected final ConfigProperty<Integer> foodSpacing = new ConfigProperty<>(2);

    @SettingSection("armor_icons")
    @SliderWidget.SliderSetting(min = 4.0f, max = 16.0f)
    protected final ConfigProperty<Integer> armorSize = new ConfigProperty<>(8);

    @SliderWidget.SliderSetting(min = 0.0f, max = 8.0f)
    protected final ConfigProperty<Integer> armorSpacing = new ConfigProperty<>(2);

    @SettingSection("padding")
    @SliderWidget.SliderSetting(min = 0.0f, max = 8.0f)
    protected final ConfigProperty<Integer> paddingTop = new ConfigProperty<>(2);

    @SliderWidget.SliderSetting(min = 0.0f, max = 8.0f)
    protected final ConfigProperty<Integer> paddingBottom = new ConfigProperty<>(2);

    @SliderWidget.SliderSetting(min = 0.0f, max = 8.0f)
    protected final ConfigProperty<Integer> paddingLeft = new ConfigProperty<>(2);

    public int foodSize() {
        return foodSize.get();
    }

    public int foodSpacing() {
        return foodSpacing.get();
    }

    public int armorSize() {
        return armorSize.get();
    }

    public int armorSpacing() {
        return armorSpacing.get();
    }

    public int paddingTop() {
        return this.paddingTop.get();
    }

    public int pattingBottom() {
        return this.paddingBottom.get();
    }

    public int pattingLeft() {
        return this.paddingLeft.get();
    }

}
