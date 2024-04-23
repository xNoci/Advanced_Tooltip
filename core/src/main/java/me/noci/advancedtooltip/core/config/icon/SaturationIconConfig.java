package me.noci.advancedtooltip.core.config.icon;

import me.noci.advancedtooltip.core.config.SaturationType;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingOrder;

public class SaturationIconConfig extends IconConfig {

    @DropdownSetting
    @SettingOrder(SettingOrder.Order.FIRST)
    private final ConfigProperty<SaturationType> saturationType = new ConfigProperty<>(SaturationType.CURRENT_SATURATION);

    public SaturationType saturationType() {
        return this.saturationType.get();
    }

}
