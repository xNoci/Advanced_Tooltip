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

        for (DurabilityColor value : DurabilityColor.values()) {
            if (durabilityPercentage >= value.durability) {
                return value.color;
            }
        }

        return DurabilityColor.DARK_RED.color;
    }

    private enum DurabilityColor {
        DARK_GREEN(TextColor.color(0x00AA00), 0.75f),
        GREEN(TextColor.color(0x55FF55), 0.45f),
        YELLOW(TextColor.color(0xFFFF55), 0.25f),
        RED(TextColor.color(0xFF5555), 0.15f),
        DARK_RED(TextColor.color(0xAA0000), 0);

        private final TextColor color;
        private final float durability;

        DurabilityColor(TextColor color, float durability) {
            this.color = color;
            this.durability = durability;
        }
    }

}
