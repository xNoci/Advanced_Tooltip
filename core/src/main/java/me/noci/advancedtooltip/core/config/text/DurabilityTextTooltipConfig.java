package me.noci.advancedtooltip.core.config.text;

import me.noci.advancedtooltip.core.config.DurabilityType;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;
import net.labymod.api.configuration.settings.annotation.SettingSection;

public class DurabilityTextTooltipConfig extends TextTooltipConfig {

    private static final TextColor DARK_GREEN = TextColor.color(0x00AA00);
    private static final TextColor GREEN = TextColor.color(0x55FF55);
    private static final TextColor YELLOW = TextColor.color(0xFFFF55);
    private static final TextColor RED = TextColor.color(0xFF5555);
    private static final TextColor DARK_RED = TextColor.color(0xAA0000);

    @SettingSection("durabilitySection")
    @DropdownSetting
    @SettingRequires("enabled")
    @IntroducedIn(namespace = "advancedtooltip", value = "1.6.0")
    private final ConfigProperty<DurabilityType> durabilityType = new ConfigProperty<>(DurabilityType.VANILLA);

    @SwitchSetting
    @IntroducedIn(namespace = "advancedtooltip", value = "1.7.0")
    private final ConfigProperty<Boolean> textColorReflectsDurability = new ConfigProperty<>(false);

    public DurabilityType durabilityType() {
        return this.durabilityType.get();
    }

    public TextColor textColor(float durabilityPercentage) {
        if (!textColorReflectsDurability.get()) {
            return textColor();
        }

        if (durabilityPercentage >= 0.75) {
            return DARK_GREEN;
        }

        if (durabilityPercentage >= 0.45) {
            return GREEN;
        }

        if (durabilityPercentage >= 0.25) {
            return YELLOW;
        }

        if (durabilityPercentage >= 0.15) {
            return RED;
        }

        return DARK_RED;
    }
}
