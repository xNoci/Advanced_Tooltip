package me.noci.advancedtooltip.core.listener;

import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import me.noci.advancedtooltip.core.config.AdvancedTooltipConfiguration;
import me.noci.advancedtooltip.core.utils.ItemQuery;
import me.noci.advancedtooltip.core.utils.MapLocation;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.world.ItemStackTooltipEvent;
import net.labymod.api.util.I18n;

import java.util.List;

public class ItemStackTooltipListener {

    private final AdvancedTooltipAddon addon;
    private final AdvancedTooltipConfiguration config;
    private final ItemQuery itemQuery;

    public ItemStackTooltipListener(AdvancedTooltipAddon addon, ItemQuery itemQuery) {
        this.addon = addon;
        this.config = addon.configuration();
        this.itemQuery = itemQuery;
    }

    @Subscribe
    public void onToolTip(ItemStackTooltipEvent event) {
        ItemStack itemStack = event.itemStack();
        if (!AdvancedTooltipAddon.enabled()) return;

        List<Component> tooltip = event.getTooltipLines();

        if (config.showAnvilUses().get()) {
            handleAnvilUses(itemStack, tooltip);
        }

        if (config.discSignalStrength().get()) {
            handleDiscSignalStrength(itemStack, tooltip);
        }

        if (config.explorerMapCoordinates().get()) {
            handleExplorerMap(itemStack, tooltip);
        }

    }

    private void handleAnvilUses(ItemStack itemStack, List<Component> tooltip) {
        int usages = itemQuery.getAnvilUsages(itemStack);
        if (usages == ItemQuery.INVALID_ITEM) return;

        tooltip(tooltip, "anvil_usages", usages);
    }

    private void handleDiscSignalStrength(ItemStack itemStack, List<Component> tooltip) {
        int strength = itemQuery.getDiscSignalStrengt(itemStack);
        if (strength == ItemQuery.INVALID_ITEM) return;

        tooltip(tooltip, "disc_signal_strength", strength);
    }

    private void handleExplorerMap(ItemStack itemStack, List<Component> tooltip) {
        MapLocation mapLocation = itemQuery.getExplorerMapLocation(itemStack);
        if (mapLocation == null) return;

        tooltip(tooltip, "explorer_map." + mapLocation.getType().name().toLowerCase(), mapLocation.getX(), mapLocation.getZ());
    }

    private void tooltip(List<Component> tooltip, String key, Object... value) {
        String text = I18n.translate("advancedtooltip.tooltip." + key, value);
        tooltip.add(Component.text(text));
    }

}
