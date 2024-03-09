package me.noci.advancedtooltip.core.listener;

import me.noci.advancedtooltip.core.TooltipAddon;
import me.noci.advancedtooltip.core.config.text.DisplayComponentTextTooltipConfig;
import me.noci.advancedtooltip.core.referenceable.InventoryHelper;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;

public class KeyPressListener {

    private final InventoryHelper inventoryHelper;
    private final DisplayComponentTextTooltipConfig config;

    public KeyPressListener(TooltipAddon addon, InventoryHelper inventoryHelper) {
        this.inventoryHelper = inventoryHelper;
        this.config = addon.configuration().developerSettings().displayComponent();
    }

    @Subscribe
    public void keyPress(KeyEvent event) {
        if (!inventoryHelper.isInventoryOpen()) return;
        if (event.key() != config.displayItemData()) return;
        if (event.state() != KeyEvent.State.PRESS) return;

        config.toggleDisplayItemData();
    }

}
