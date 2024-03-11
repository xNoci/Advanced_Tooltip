package me.noci.advancedtooltip.core.config.icon;

import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ParentSwitch;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.CustomTranslation;
import net.labymod.api.configuration.settings.annotation.SettingRequires;
import net.labymod.api.configuration.settings.annotation.SettingSection;

@SettingRequires("enabled")
public class IconConfig extends Config {

    private static final String TANSLATION_KEY_PREFIX = "advancedtooltip.settings.iconConfig";

    public static final int DEFAULT_ICON_SIZE = 8;
    public static final int DEFAULT_PADDING = 2;

    @ParentSwitch
    @SwitchSetting
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

    @SettingSection(value = "icon", translation = TANSLATION_KEY_PREFIX)
    @SliderSetting(min = 4.0f, max = 16.0f)
    @CustomTranslation(TANSLATION_KEY_PREFIX + ".iconSize")
    protected final ConfigProperty<Integer> iconSize = new ConfigProperty<>(DEFAULT_ICON_SIZE);

    @SliderSetting(min = 0.0f, max = 8.0f)
    @CustomTranslation(TANSLATION_KEY_PREFIX + ".iconSpacing")
    protected final ConfigProperty<Integer> iconSpacing = new ConfigProperty<>(DEFAULT_PADDING);

    @SettingSection(value = "padding", translation = TANSLATION_KEY_PREFIX)
    @SliderSetting(min = 0.0f, max = 16.0f)
    @CustomTranslation(TANSLATION_KEY_PREFIX + ".paddingTop")
    protected final ConfigProperty<Integer> paddingTop = new ConfigProperty<>(DEFAULT_PADDING);

    @SliderSetting(min = 0.0f, max = 16.0f)
    @CustomTranslation(TANSLATION_KEY_PREFIX + ".paddingBottom")
    protected final ConfigProperty<Integer> paddingBottom = new ConfigProperty<>(DEFAULT_PADDING);

    @SliderSetting(min = 0.0f, max = 16.0f)
    @CustomTranslation(TANSLATION_KEY_PREFIX + ".paddingLeft")
    protected final ConfigProperty<Integer> paddingLeft = new ConfigProperty<>(DEFAULT_PADDING);

    @SliderSetting(min = 0.0f, max = 16.0f)
    @CustomTranslation(TANSLATION_KEY_PREFIX + ".paddingRight")
    protected final ConfigProperty<Integer> paddingRight = new ConfigProperty<>(DEFAULT_PADDING);


    public boolean enabled() {
        return this.enabled.get();
    }

    public int iconSize() {
        return this.iconSize.get();
    }

    public int iconSpacing() {
        return this.iconSpacing.get();
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

    public int pattingRight() {
        return this.paddingRight.get();
    }

}
