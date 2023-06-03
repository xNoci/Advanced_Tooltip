package me.noci.advancedtooltip.core.config;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingDevelopment;
import net.labymod.api.configuration.settings.annotation.SettingSection;

@SuppressWarnings("FieldMayBeFinal")
@ConfigName("settings")
public class AdvancedTooltipConfiguration extends AddonConfig {

    @SwitchSetting
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

    @SettingSection("food_info")
    @DropdownSetting
    private final ConfigProperty<SaturationType> saturationLevel = new ConfigProperty<>(SaturationType.CURRENT_SATURATION);
    @SwitchSetting
    private final ConfigProperty<Boolean> foodLevel = new ConfigProperty<>(true);

    @SettingSection("developer")
    @SwitchSetting @SettingDevelopment
    private final ConfigProperty<Boolean> debugMode = new ConfigProperty<>(false);

    @Override
    public ConfigProperty<Boolean> enabled() {
        return this.enabled;
    }

    public ConfigProperty<SaturationType> saturationLevel() {
        return this.saturationLevel;
    }

    public ConfigProperty<Boolean> foodLevel() {
        return this.foodLevel;
    }

    public ConfigProperty<Boolean> debugMode() {
        return this.debugMode;
    }
}
