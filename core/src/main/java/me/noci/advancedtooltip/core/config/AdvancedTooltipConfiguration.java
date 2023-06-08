package me.noci.advancedtooltip.core.config;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.VersionCompatibility;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingDevelopment;
import net.labymod.api.configuration.settings.annotation.SettingSection;

@SuppressWarnings("FieldMayBeFinal")
@ConfigName("settings")
public class AdvancedTooltipConfiguration extends AddonConfig {

    @SwitchSetting
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

    @SettingSection("items")
    @SwitchSetting
    private final ConfigProperty<Boolean> showAnvilUses = new ConfigProperty<>(true);

    @SwitchSetting @VersionCompatibility("1.19<1.20")
    private final ConfigProperty<Boolean> discSignalStrength = new ConfigProperty<>(true);

    @SettingSection("food_info")
    @DropdownSetting @VersionCompatibility("1.19<1.20")
    private final ConfigProperty<SaturationType> saturationLevel = new ConfigProperty<>(SaturationType.CURRENT_SATURATION);

    @SwitchSetting @VersionCompatibility("1.19<1.20")
    private final ConfigProperty<Boolean> foodLevel = new ConfigProperty<>(true);

    @VersionCompatibility("1.19<1.20")
    private final FoodIconSubSetting foodIconSetting = new FoodIconSubSetting();

    @SettingSection("developer")
    @SwitchSetting @SettingDevelopment
    private final ConfigProperty<Boolean> debugMode = new ConfigProperty<>(false);

    @Override
    public ConfigProperty<Boolean> enabled() {
        return this.enabled;
    }

    public ConfigProperty<Boolean> showAnvilUses() {
        return this.showAnvilUses;
    }

    public ConfigProperty<Boolean> discSignalStrength() {
        return this.discSignalStrength;
    }

    public ConfigProperty<SaturationType> saturationLevel() {
        return this.saturationLevel;
    }

    public ConfigProperty<Boolean> foodLevel() {
        return this.foodLevel;
    }

    public ConfigProperty<Integer> iconSize() {
        return this.foodIconSetting.iconSize;
    }

    public ConfigProperty<Integer> iconSpacing() {
        return this.foodIconSetting.iconSpacing;
    }

    public ConfigProperty<Boolean> debugMode() {
        return this.debugMode;
    }
}
