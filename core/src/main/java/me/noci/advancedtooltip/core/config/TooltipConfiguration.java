package me.noci.advancedtooltip.core.config;

import lombok.Getter;
import me.noci.advancedtooltip.core.config.icon.IconConfig;
import me.noci.advancedtooltip.core.config.icon.SaturationIconConfig;
import me.noci.advancedtooltip.core.config.text.DisplayComponentTextTooltipConfig;
import me.noci.advancedtooltip.core.config.text.TextTooltipConfig;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.annotation.VersionCompatibility;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;

@Getter
@ConfigName("settings")
public class TooltipConfiguration extends AddonConfig {

    public static final String LATEST_SUPPORTED_VERSION = "1.20.4";

    @SwitchSetting
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

    private final TextTooltipConfig globalColor = new TextTooltipConfig();

    @SettingSection("tooltip_text")
    private final TextTooltipConfig itemDurability = new TextTooltipConfig();

    private final TextTooltipConfig anvilUsages = new TextTooltipConfig();

    private final TextTooltipConfig discSignalStrength = new TextTooltipConfig();

    private final TextTooltipConfig commandBlockCommand = new TextTooltipConfig();

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

}
