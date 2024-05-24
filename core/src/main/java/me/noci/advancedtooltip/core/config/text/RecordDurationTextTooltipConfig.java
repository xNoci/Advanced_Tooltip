package me.noci.advancedtooltip.core.config.text;

import me.noci.advancedtooltip.core.config.DurationTimeUnit;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;
import net.labymod.api.configuration.settings.annotation.SettingSection;

public class RecordDurationTextTooltipConfig extends TextTooltipConfig implements DurationTextTooltipConfig {

    @SettingSection("recordDurationSection")
    @DropdownSetting
    @SettingRequires("enabled")
    private final ConfigProperty<DurationTimeUnit> durationTimeUnit = new ConfigProperty<>(DurationTimeUnit.MINUTES);

    @Override
    public DurationTimeUnit durationUnit() {
        return this.durationTimeUnit.get();
    }

    @Override
    public String configKey() {
        return "record_length";
    }

}
