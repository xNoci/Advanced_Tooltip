package me.noci.advancedtooltip.core.config;

import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.widget.widgets.input.KeybindWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingExperimental;

public class DeveloperSubSetting extends Config {

    @SwitchWidget.SwitchSetting
    private final ConfigProperty<Boolean> debugMode = new ConfigProperty<>(false);

    @SwitchWidget.SwitchSetting @SettingExperimental
    private final ConfigProperty<Boolean> togglePrettyPrintNBT = new ConfigProperty<>(false);

    @KeybindWidget.KeyBindSetting
    private final ConfigProperty<Key> prettyPrintNBT = new ConfigProperty<>(Key.Y);

    @KeybindWidget.KeyBindSetting
    private final ConfigProperty<Key> printWithArrayData = new ConfigProperty<>(Key.L_ALT);

    public ConfigProperty<Boolean> debugMode() {
        return this.debugMode;
    }

    public ConfigProperty<Key> prettyPrintNBT() {
        return this.prettyPrintNBT;
    }

    public ConfigProperty<Key> printWithArrayData() {
        return this.printWithArrayData;
    }

    private boolean nbtDataShownToggled = false;

    public void togglePrintNBT() {
        if (!togglePrettyPrintNBT.get()) return;
        nbtDataShownToggled = !nbtDataShownToggled;
    }

    public boolean showNBTData() {
        if (!togglePrettyPrintNBT.get() && prettyPrintNBT.get().isPressed()) return true;

        if (togglePrettyPrintNBT.get()) {
            return nbtDataShownToggled;
        }

        return false;
    }


}
