package me.noci.advancedtooltip.core.config;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.VersionCompatibility;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingExperimental;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.util.Color;

@SuppressWarnings("FieldMayBeFinal")
@ConfigName("settings")
public class AdvancedTooltipConfiguration extends AddonConfig {

    @SwitchSetting
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

    @SettingSection("items")
    @SwitchSetting
    private final ConfigProperty<Boolean> showAnvilUses = new ConfigProperty<>(true);

    @SwitchSetting
    private final ConfigProperty<Boolean> discSignalStrength = new ConfigProperty<>(true);

    @SwitchSetting @VersionCompatibility("1.12<1.20.1")
    private final ConfigProperty<Boolean> explorerMapCoordinates = new ConfigProperty<>(true);

    @SwitchSetting @VersionCompatibility("1.16<1.20.1")
    private final ConfigProperty<Boolean> showSuspiciousStewEffect = new ConfigProperty<>(true);

    @ColorPickerWidget.ColorPickerSetting(chroma = true)
    private final ConfigProperty<Color> tooltipTextColor = new ConfigProperty<>(Color.WHITE);

    @SettingSection("food_info")
    @DropdownSetting @VersionCompatibility("1.19<1.20.1")
    private final ConfigProperty<SaturationType> saturationLevel = new ConfigProperty<>(SaturationType.CURRENT_SATURATION);

    @SwitchSetting @VersionCompatibility("1.19<1.20.1")
    private final ConfigProperty<Boolean> foodLevel = new ConfigProperty<>(true);

    @VersionCompatibility("1.19<1.20.1")
    private final FoodIconSubSetting foodIconSettings = new FoodIconSubSetting();

    @SettingSection("developer") @SettingExperimental
    private final DeveloperSubSetting developerSettings = new DeveloperSubSetting();

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

    public ConfigProperty<Boolean> explorerMapCoordinates() {
        return this.explorerMapCoordinates;
    }

    public ConfigProperty<Boolean> showSuspiciousStewEffect() {
        return this.showSuspiciousStewEffect;
    }

    public ConfigProperty<Color> tooltipColor() {
        return this.tooltipTextColor;
    }

    public ConfigProperty<SaturationType> saturationLevel() {
        return this.saturationLevel;
    }

    public ConfigProperty<Boolean> foodLevel() {
        return this.foodLevel;
    }

    public ConfigProperty<Integer> iconSize() {
        return this.foodIconSettings.iconSize;
    }

    public ConfigProperty<Integer> iconSpacing() {
        return this.foodIconSettings.iconSpacing;
    }

    public DeveloperSubSetting developerSettings() {
        return this.developerSettings;
    }
}
