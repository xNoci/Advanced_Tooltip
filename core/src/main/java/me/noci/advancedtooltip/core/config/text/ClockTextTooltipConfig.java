package me.noci.advancedtooltip.core.config.text;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;

public class ClockTextTooltipConfig extends TextTooltipConfig {

    @SwitchSetting
    @SettingRequires("enabled")
    @IntroducedIn(namespace = "advancedtooltip", value = "1.6.0")
    private final ConfigProperty<Boolean> format24Hours = new ConfigProperty<>(true);

    public boolean format24Hours() {
        return this.format24Hours.get();
    }


}
