package me.noci.advancedtooltip.core.config.text;

import me.noci.advancedtooltip.core.TooltipAddon;
import me.noci.advancedtooltip.core.config.TooltipConfiguration;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget.ColorPickerSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ParentSwitch;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.SettingHandler;
import net.labymod.api.util.Color;

public class TextTooltipConfig extends Config {

    @ParentSwitch
    @SwitchSetting
    private final ConfigProperty<Boolean> enabled;

    @ColorPickerSetting(chroma = true)
    private final ConfigProperty<Color> textColor = new ConfigProperty<>(Color.WHITE).withHandler(new TextColorEnabledHandler(this));

    public TextTooltipConfig() {
        this(true);
    }

    public TextTooltipConfig(boolean enabled) {
        this.enabled = new ConfigProperty<>(enabled);
    }

    public boolean enabled() {
        return this.enabled.get();
    }

    public TextColor textColor() {
        TooltipConfiguration config = TooltipAddon.get().configuration();
        TextTooltipConfig globalColor = config.globalColor();
        if (globalColor.enabled()) return TextColor.color(globalColor.textColor.get().get());
        return TextColor.color(textColor.get().get());
    }

    private record TextColorEnabledHandler(TextTooltipConfig setting) implements SettingHandler {
        @Override
        public void created(Setting setting) {

        }

        @Override
        public void initialized(Setting setting) {

        }

        @Override
        public boolean isEnabled(Setting setting) {
            TooltipConfiguration config = TooltipAddon.get().configuration();
            TextTooltipConfig globalColor = config.globalColor();
            if (this.setting == globalColor) {
                return globalColor.enabled();
            }
            return !globalColor.enabled();
        }
    }
}
