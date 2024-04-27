package me.noci.advancedtooltip.core.config.text;

import me.noci.advancedtooltip.core.TooltipAddon;
import me.noci.advancedtooltip.core.config.TooltipConfiguration;
import me.noci.advancedtooltip.core.utils.ToggleBind;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.widget.widgets.input.KeybindWidget.KeyBindSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.annotation.VersionCompatibility;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;

public class DisplayComponentTextTooltipConfig extends TextTooltipConfig {

    @SettingSection("itemDataSection")
    @SwitchSetting
    private final ConfigProperty<Boolean> toggleDisplayItemData = new ConfigProperty<>(false).addChangeListener(isToggle -> {
        if (isToggle) return;
        this.itemDataBind.toggle(false);
    });

    @KeyBindSetting
    private final ConfigProperty<Key> displayItemData = new ConfigProperty<>(Key.Y);

    @KeyBindSetting
    private final ConfigProperty<Key> printWithNbtArrayData = new ConfigProperty<>(Key.L_ALT);

    @KeyBindSetting
    @VersionCompatibility("1.20.5<" + TooltipConfiguration.LATEST_SUPPORTED_VERSION)
    @IntroducedIn(namespace = "advancedtooltip", value = "1.6.0")
    private final ConfigProperty<Key> expandComponents = new ConfigProperty<>(Key.L_CONTROL);

    private final transient ToggleBind itemDataBind = new ToggleBind(displayItemData::get, () -> {
        if (!toggleDisplayItemData.get()) return false;
        return TooltipAddon.inventoryHelper().isInventoryOpen();
    });

    public Key printWithNbtArrayData() {
        return printWithNbtArrayData.get();
    }

    public Key expandComponents() {
        return expandComponents.get();
    }

    public boolean displayItemData() {
        return itemDataBind.toggledOr(() -> displayItemData.get().isPressed());
    }

}
