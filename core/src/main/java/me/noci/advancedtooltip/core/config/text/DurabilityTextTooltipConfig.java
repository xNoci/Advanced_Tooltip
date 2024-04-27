package me.noci.advancedtooltip.core.config.text;

import me.noci.advancedtooltip.core.config.DurabilityType;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;
import net.labymod.api.configuration.settings.annotation.SettingSection;

public class DurabilityTextTooltipConfig extends TextTooltipConfig {


    @SettingSection("durabilitySection")
    @DropdownSetting
    @SettingRequires("enabled")
    @IntroducedIn(namespace = "advancedtooltip", value = "1.6.0")
    private final ConfigProperty<DurabilityType> durabilityType = new ConfigProperty<>(DurabilityType.VANILLA);

    public DurabilityType durabilityType() {
        return this.durabilityType.get();
    }

}
