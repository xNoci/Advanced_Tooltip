package me.noci.advancedtooltip.core.listener;

import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import me.noci.advancedtooltip.core.config.AdvancedTooltipConfiguration;
import me.noci.advancedtooltip.core.referenceable.InventoryManager;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;

public class KeyPressListener {

    private final InventoryManager inventoryManager;
    private final AdvancedTooltipConfiguration config;

    public KeyPressListener(AdvancedTooltipAddon addon, InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
        this.config = addon.configuration();
    }

    @Subscribe
    public void keyPress(KeyEvent event) {
        if (!AdvancedTooltipAddon.enabled()) return;

        if (!inventoryManager.isInventoryOpen()) return;
        if (event.key() != config.developerSettings().prettyPrintNBT().get()) return;
        if (event.state() != KeyEvent.State.PRESS) return;

        config.developerSettings().togglePrintNBT();
    }

}
