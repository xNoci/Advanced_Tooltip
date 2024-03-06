package me.noci.advancedtooltip.core.listener;

import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import me.noci.advancedtooltip.core.config.AdvancedTooltipConfiguration;
import me.noci.advancedtooltip.core.referenceable.InventoryHelper;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;

public class KeyPressListener {

    private final InventoryHelper inventoryHelper;
    private final AdvancedTooltipConfiguration config;

    public KeyPressListener(AdvancedTooltipAddon addon, InventoryHelper inventoryHelper) {
        this.inventoryHelper = inventoryHelper;
        this.config = addon.configuration();
    }

    @Subscribe
    public void keyPress(KeyEvent event) {
        if (!inventoryHelper.isInventoryOpen()) return;
        if (event.key() != config.developerSettings().displayItemData().get()) return;
        if (event.state() != KeyEvent.State.PRESS) return;

        config.developerSettings().toggleDisplayItemData();
    }

}
