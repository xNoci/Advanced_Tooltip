package me.noci.advancedtooltip.core.config;

import lombok.Getter;
import me.noci.advancedtooltip.core.config.text.DisplayComponentTextTooltipConfig;
import me.noci.advancedtooltip.core.config.text.TextTooltipConfig;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.settings.annotation.SettingDevelopment;

public class DeveloperSubSetting extends Config {

    @Getter
    @SettingDevelopment
    //TODO Disable by default
    private final TextTooltipConfig debugMode = new TextTooltipConfig();

    @Getter
    private final DisplayComponentTextTooltipConfig displayComponent = new DisplayComponentTextTooltipConfig();

}
