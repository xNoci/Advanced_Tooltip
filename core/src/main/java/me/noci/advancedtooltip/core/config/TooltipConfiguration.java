package me.noci.advancedtooltip.core.config;

import lombok.Getter;
import me.noci.advancedtooltip.core.config.icon.IconConfig;
import me.noci.advancedtooltip.core.config.icon.SaturationIconConfig;
import me.noci.advancedtooltip.core.config.text.*;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.annotation.VersionCompatibility;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.util.Color;

@Getter
@ConfigName("settings")
public class TooltipConfiguration extends AddonConfig {

    public static final String LATEST_SUPPORTED_VERSION = "1.21.1";

    @SwitchSetting
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

    @SwitchSetting
    @VersionCompatibility("1.20.5<" + LATEST_SUPPORTED_VERSION)
    @IntroducedIn(namespace = "advancedtooltip", value = "1.6.0")
    private final ConfigProperty<Boolean> ignoreHideTooltip = new ConfigProperty<>(false);

    @ColorPickerWidget.ColorPickerSetting(chroma = true)
    private final ConfigProperty<Color> globalColor = new ConfigProperty<>(Color.WHITE);

    @SettingSection("tooltip_text")
    private final DurabilityTextTooltipConfig itemDurability = new DurabilityTextTooltipConfig();

    private final TextTooltipConfig anvilUsages = new TextTooltipConfig();

    private final TextTooltipConfig discSignalStrength = new TextTooltipConfig();

    @VersionCompatibility("1.19.2<" + LATEST_SUPPORTED_VERSION)
    @IntroducedIn(namespace = "advancedtooltip", value = "1.8.0")
    private final RecordDurationTextTooltipConfig recordDuration = new RecordDurationTextTooltipConfig();

    private final TextTooltipConfig commandBlockCommand = new TextTooltipConfig();

    @IntroducedIn(namespace = "advancedtooltip", value = "1.6.0")
    private final BurnDurationTextTooltipConfig burnDuration = new BurnDurationTextTooltipConfig();

    //TODO Add smithing template clone material info

    @IntroducedIn(namespace = "advancedtooltip", value = "1.7.0")
    private final TextTooltipConfig compassTarget = new TextTooltipConfig(false);

    @VersionCompatibility("1.8<1.20.4")
    @IntroducedIn(namespace = "advancedtooltip", value = "1.6.0")
    private final TextTooltipConfig miningLevel = new TextTooltipConfig(false);

    @IntroducedIn(namespace = "advancedtooltip", value = "1.6.0")
    private final MiningSpeedTextTooltipConfig miningSpeed = new MiningSpeedTextTooltipConfig();

    @IntroducedIn(namespace = "advancedtooltip", value = "1.6.0")
    private final ClockTextTooltipConfig clockTime = new ClockTextTooltipConfig();

    @VersionCompatibility("1.8.9<" + LATEST_SUPPORTED_VERSION)
    private final DisplayComponentTextTooltipConfig displayComponent = new DisplayComponentTextTooltipConfig();

    @IntroducedIn(namespace = "advancedtooltip", value = "1.5.0")
    private final TextTooltipConfig signText = new TextTooltipConfig();

    @VersionCompatibility("1.12<" + LATEST_SUPPORTED_VERSION)
    private final TextTooltipConfig mapDecoration = new TextTooltipConfig();

    @VersionCompatibility("1.16<" + LATEST_SUPPORTED_VERSION)
    private final TextTooltipConfig suspiciousStewEffect = new TextTooltipConfig();

    @SettingSection("tooltip_icon")
    @VersionCompatibility("1.19.4<" + LATEST_SUPPORTED_VERSION)
    private final SaturationIconConfig saturationIcons = new SaturationIconConfig();

    @VersionCompatibility("1.19.4<" + LATEST_SUPPORTED_VERSION)
    private final IconConfig nutritionIcons = new IconConfig();

    @VersionCompatibility("1.19.4<" + LATEST_SUPPORTED_VERSION)
    private final IconConfig armorIcons = new IconConfig();

    public boolean ignoreHideTooltip() {
        return this.ignoreHideTooltip.get();
    }

}
