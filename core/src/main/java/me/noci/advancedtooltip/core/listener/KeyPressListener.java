package me.noci.advancedtooltip.core.listener;

import me.noci.advancedtooltip.core.TooltipAddon;
import me.noci.advancedtooltip.core.config.TooltipConfiguration;
import me.noci.advancedtooltip.core.referenceable.InventoryHelper;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;

public class KeyPressListener {

    private final InventoryHelper inventoryHelper;
    private final TooltipConfiguration config;

    public KeyPressListener(TooltipAddon addon, InventoryHelper inventoryHelper) {
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
