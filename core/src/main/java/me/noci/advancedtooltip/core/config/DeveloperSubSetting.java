package me.noci.advancedtooltip.core.config;

import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.widget.widgets.input.KeybindWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.VersionCompatibility;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingExperimental;

public class DeveloperSubSetting extends Config {

    @SwitchWidget.SwitchSetting
    private final ConfigProperty<Boolean> debugMode = new ConfigProperty<>(false);

    @SwitchWidget.SwitchSetting @SettingExperimental
    private final ConfigProperty<Boolean> toggleDisplayItemData = new ConfigProperty<>(false);

    @KeybindWidget.KeyBindSetting
    private final ConfigProperty<Key> displayItemData = new ConfigProperty<>(Key.Y);

    @KeybindWidget.KeyBindSetting
    private final ConfigProperty<Key> printWithNbtArrayData = new ConfigProperty<>(Key.L_ALT);

    @KeybindWidget.KeyBindSetting
    @VersionCompatibility("1.20.5<" + AdvancedTooltipConfiguration.LATEST_SUPPORTED_VERSION)
    private final ConfigProperty<Key> expandComponents = new ConfigProperty<>(Key.L_CONTROL);

    public ConfigProperty<Boolean> debugMode() {
        return this.debugMode;
    }

    public ConfigProperty<Key> displayItemData() {
        return this.displayItemData;
    }

    public ConfigProperty<Key> printWithNbtArrayData() {
        return this.printWithNbtArrayData;
    }

    public ConfigProperty<Key> expandComponents() {
        return this.expandComponents;
    }

    private boolean nbtDataShownToggled = false;

    public void toggleDisplayItemData() {
        if (!toggleDisplayItemData.get()) return;
        nbtDataShownToggled = !nbtDataShownToggled;
    }

    public boolean isDisplayItemData() {
        if (!toggleDisplayItemData.get() && displayItemData.get().isPressed()) return true;

        if (toggleDisplayItemData.get()) {
            return nbtDataShownToggled;
        }

        return false;
    }


}
