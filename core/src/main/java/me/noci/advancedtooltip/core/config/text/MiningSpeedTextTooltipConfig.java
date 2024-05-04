package me.noci.advancedtooltip.core.config.text;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;
import net.labymod.api.configuration.settings.annotation.SettingSection;

public class MiningSpeedTextTooltipConfig extends TextTooltipConfig {

    @SettingSection("miningSpeedSection")
    @SwitchSetting
    @SettingRequires("enabled")
    private final ConfigProperty<Boolean> applyEnchantments = new ConfigProperty<>(true);

    public boolean applyEnchantments() {
        return this.applyEnchantments.get();
    }

}
