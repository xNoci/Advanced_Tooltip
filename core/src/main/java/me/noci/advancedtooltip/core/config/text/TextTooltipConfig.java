package me.noci.advancedtooltip.core.config.text;

import me.noci.advancedtooltip.core.TooltipAddon;
import me.noci.advancedtooltip.core.config.TooltipConfiguration;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget.ColorPickerSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.annotation.ShowSettingInParent;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.CustomTranslation;
import net.labymod.api.configuration.settings.annotation.SettingRequires;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.util.Color;

public class TextTooltipConfig extends Config implements TooltipConfig {

    private static final String TRANSLATION_KEY_PREFIX = "advancedtooltip.settings.textSetting";

    @ShowSettingInParent
    @SwitchSetting
    private final ConfigProperty<Boolean> enabled;

    @SettingSection(value = "colorSection", translation = TRANSLATION_KEY_PREFIX)
    @SwitchSetting
    @CustomTranslation(TRANSLATION_KEY_PREFIX + ".useGlobalColor")
    @IntroducedIn(namespace = "advancedtooltip", value = "1.7.0")
    private final ConfigProperty<Boolean> useGlobalColor = new ConfigProperty<>(true);

    @ColorPickerSetting(chroma = true)
    @SettingRequires(value = "useGlobalColor", invert = true)
    private final ConfigProperty<Color> textColor = new ConfigProperty<>(Color.WHITE);

    public TextTooltipConfig() {
        this(true);
    }

    public TextTooltipConfig(boolean enabled) {
        this.enabled = new ConfigProperty<>(enabled);
    }

    @Override
    public boolean enabled() {
        return this.enabled.get();
    }

    @Override
    public TextColor textColor() {
        if (useGlobalColor.get()) {
            TooltipConfiguration config = TooltipAddon.get().configuration();
            return TextColor.color(config.globalColor().get().get());
        }
        return TextColor.color(textColor.get().get());
    }

}
