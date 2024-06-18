package me.noci.advancedtooltip.core.config.text;

import me.noci.advancedtooltip.core.config.DurationTimeUnit;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;
import net.labymod.api.configuration.settings.annotation.SettingSection;

public class BurnDurationTextTooltipConfig extends TextTooltipConfig implements DurationTextTooltipConfig {

    @SettingSection("burnDurationSection")
    @DropdownSetting
    @SettingRequires("enabled")
    @IntroducedIn(namespace = "advancedtooltip", value = "1.7.0")
    private final ConfigProperty<DurationTimeUnit> durationTimeUnit = new ConfigProperty<>(DurationTimeUnit.MINUTES);

    @Override
    public DurationTimeUnit durationUnit() {
        return this.durationTimeUnit.get();
    }

    @Override
    public String configKey() {
        return "burn_time";
    }

}
