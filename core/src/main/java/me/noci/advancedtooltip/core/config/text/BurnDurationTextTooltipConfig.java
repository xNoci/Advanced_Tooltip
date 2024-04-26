package me.noci.advancedtooltip.core.config.text;

import me.noci.advancedtooltip.core.config.BurnDurationTimeUnit;
import me.noci.advancedtooltip.core.config.DurabilityType;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;

public class BurnDurationTextTooltipConfig extends TextTooltipConfig {


    @DropdownSetting
    @SettingRequires("enabled")
    @IntroducedIn(namespace = "advancedtooltip", value = "1.7.0")
    private final ConfigProperty<BurnDurationTimeUnit> burnDurationTimeUnit = new ConfigProperty<>(BurnDurationTimeUnit.MINUTES);

    public BurnDurationTimeUnit burnDurationTimeUnit() {
        return this.burnDurationTimeUnit.get();
    }


}
