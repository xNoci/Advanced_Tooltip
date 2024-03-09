package me.noci.advancedtooltip.core.config;

import lombok.Getter;
import me.noci.advancedtooltip.core.config.icon.IconConfig;
import me.noci.advancedtooltip.core.config.icon.SaturationIconConfig;
import me.noci.advancedtooltip.core.config.text.TextTooltipConfig;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.annotation.VersionCompatibility;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingExperimental;
import net.labymod.api.configuration.settings.annotation.SettingSection;

@SuppressWarnings("FieldMayBeFinal")
@ConfigName("settings")
public class TooltipConfiguration extends AddonConfig {

    public static final String LATEST_SUPPORTED_VERSION = "1.20.4";

    @Getter
    @SwitchSetting
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

    @SettingSection("tooltip_text")
    @Getter
    private final TextTooltipConfig globalColor = new TextTooltipConfig();

    @Getter
    private final TextTooltipConfig itemDurability = new TextTooltipConfig();

    @Getter
    private final TextTooltipConfig anvilUsages = new TextTooltipConfig();

    @Getter
    private final TextTooltipConfig discSignalStrength = new TextTooltipConfig();

    @Getter
    private final TextTooltipConfig commandBlockCommand = new TextTooltipConfig();

    @Getter
    @IntroducedIn(namespace = "advancedtooltip", value = "1.5.0")
    private final TextTooltipConfig signText = new TextTooltipConfig();

    @Getter
    @VersionCompatibility("1.12<" + LATEST_SUPPORTED_VERSION)
    private final TextTooltipConfig mapDecoration = new TextTooltipConfig();

    @Getter
    @VersionCompatibility("1.16<" + LATEST_SUPPORTED_VERSION)
    private final TextTooltipConfig suspiciousStewEffect = new TextTooltipConfig();

    @SettingSection("tooltip_icon")
    @Getter
    @VersionCompatibility("1.19.4<" + LATEST_SUPPORTED_VERSION)
    private final SaturationIconConfig saturationIcons = new SaturationIconConfig();

    @Getter
    @VersionCompatibility("1.19.4<" + LATEST_SUPPORTED_VERSION)
    private final IconConfig nutritionIcons = new IconConfig();

    @Getter
    @VersionCompatibility("1.19.4<" + LATEST_SUPPORTED_VERSION)
    private final IconConfig armorIcons = new IconConfig();

    @Getter
    @SettingSection("developer")
    @SettingExperimental
    private final DeveloperSubSetting developerSettings = new DeveloperSubSetting();

}
