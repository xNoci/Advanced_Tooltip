package me.noci.advancedtooltip.core.listener;

import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import me.noci.advancedtooltip.core.utils.ItemQuery;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.world.ItemStackTooltipEvent;
import net.labymod.api.util.I18n;

public class ItemAnvilUsesListener {

    private final AdvancedTooltipAddon addon;
    private final ItemQuery itemQuery;

    public ItemAnvilUsesListener(AdvancedTooltipAddon addon, ItemQuery itemQuery) {
        this.addon = addon;
        this.itemQuery = itemQuery;
    }

    @Subscribe
    public void onToolTip(ItemStackTooltipEvent event) {
        ItemStack itemStack = event.itemStack();
        if (!AdvancedTooltipAddon.enabled() || !addon.configuration().showAnvilUses().get()) return;

        int usages = itemQuery.getUsages(itemStack);
        if (usages == 0) return;

        String text = I18n.translate("advancedtooltip.tooltip.anvil_usages", usages);
        event.getTooltipLines().add(Component.text(text));
    }


}
