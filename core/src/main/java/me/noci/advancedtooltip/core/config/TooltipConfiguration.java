package me.noci.advancedtooltip.core.config;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.annotation.VersionCompatibility;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingExperimental;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.util.Color;

@SuppressWarnings("FieldMayBeFinal")
@ConfigName("settings")
public class TooltipConfiguration extends AddonConfig {

    public static final String LATEST_SUPPORTED_VERSION = "1.20.4";

    @SwitchSetting
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

    @SettingSection("tooltip_text")
    @SwitchSetting
    private final ConfigProperty<Boolean> showAnvilUses = new ConfigProperty<>(true);

    @SwitchSetting
    private final ConfigProperty<Boolean> discSignalStrength = new ConfigProperty<>(true);

    @SwitchSetting @VersionCompatibility("1.12<" + LATEST_SUPPORTED_VERSION)
    private final ConfigProperty<Boolean> explorerMapCoordinates = new ConfigProperty<>(true);

    @SwitchSetting @VersionCompatibility("1.16<" + LATEST_SUPPORTED_VERSION)
    private final ConfigProperty<Boolean> showSuspiciousStewEffect = new ConfigProperty<>(true);

    @SwitchSetting
    private final ConfigProperty<Boolean> showDurability = new ConfigProperty<>(false);

    @SwitchSetting
    private final ConfigProperty<Boolean> showCommandBlockCommand = new ConfigProperty<>(true);

    @SwitchSetting
    @IntroducedIn(namespace = "advancedtooltip", value = "1.5.0")
    private final ConfigProperty<Boolean> showSignText = new ConfigProperty<>(true);

    @ColorPickerWidget.ColorPickerSetting(chroma = true)
    private final ConfigProperty<Color> tooltipTextColor = new ConfigProperty<>(Color.WHITE);

    @SettingSection("tooltip_icon")
    @DropdownSetting @VersionCompatibility("1.19.4<" + LATEST_SUPPORTED_VERSION)
    private final ConfigProperty<SaturationType> saturationLevel = new ConfigProperty<>(SaturationType.CURRENT_SATURATION);

    @SwitchSetting @VersionCompatibility("1.19.4<" + LATEST_SUPPORTED_VERSION)
    private final ConfigProperty<Boolean> foodLevel = new ConfigProperty<>(true);

    @SwitchSetting @VersionCompatibility("1.19.4<" + LATEST_SUPPORTED_VERSION)
    private final ConfigProperty<Boolean> showArmorBarIcons = new ConfigProperty<>(true);

    @VersionCompatibility("1.19.4<" + LATEST_SUPPORTED_VERSION)
    private final IconSubSetting iconSubSetting = new IconSubSetting();

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

    public ConfigProperty<Boolean> showDurability() {
        return this.showDurability;
    }

    public boolean showCommandBlockCommand() {
        return this.showCommandBlockCommand.get();
    }

    public boolean showSignText() {
        return this.showSignText.get();
    }

    public ConfigProperty<Color> tooltipColor() {
        return this.tooltipTextColor;
    }

    public SaturationType saturationType() {
        return this.saturationLevel.get();
    }

    public boolean showSaturationLevel() {
        return saturationType() != SaturationType.HIDDEN;
    }

    public boolean showFoodLevel() {
        return this.foodLevel.get();
    }

    public boolean showArmorBarIcons() {
        return this.showArmorBarIcons.get();
    }

    public IconSubSetting iconSubSetting() {
        return this.iconSubSetting;
    }

    public DeveloperSubSetting developerSettings() {
        return this.developerSettings;
    }
}
