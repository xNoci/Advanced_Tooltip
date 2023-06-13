package me.noci.advancedtooltip.core.listener;

import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import me.noci.advancedtooltip.core.config.AdvancedTooltipConfiguration;
import me.noci.advancedtooltip.core.utils.ItemQuery;
import me.noci.advancedtooltip.core.utils.MapLocation;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.world.effect.PotionEffect;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.world.ItemStackTooltipEvent;
import net.labymod.api.util.I18n;
import net.labymod.api.util.time.TimeUtil;

import java.util.List;

public class ItemStackTooltipListener {

    private final AdvancedTooltipConfiguration config;
    private final ItemQuery itemQuery;

    public ItemStackTooltipListener(AdvancedTooltipAddon addon, ItemQuery itemQuery) {
        this.config = addon.configuration();
        this.itemQuery = itemQuery;
    }

    @Subscribe
    public void onToolTip(ItemStackTooltipEvent event) {
        ItemStack itemStack = event.itemStack();
        if (!AdvancedTooltipAddon.enabled()) return;

        List<Component> tooltip = event.getTooltipLines();

        if (config.developerSettings().prettyPrintNBT().get().isPressed()) {
            String nbt = itemQuery.getItemNBTData(itemStack, config.developerSettings().printWithArrayData().get().isPressed());
            if (nbt == null) {
                tooltip(tooltip, "no_nbt_data");
                return;
            }

            tooltip(tooltip, false, "");
            for (String s : nbt.split("\n")) {
                tooltip(tooltip, false, s);
            }
            return;
        }

        if (config.showAnvilUses().get()) {
            handleAnvilUses(itemStack, tooltip);
        }

        if (config.discSignalStrength().get()) {
            handleDiscSignalStrength(itemStack, tooltip);
        }

        if (config.explorerMapCoordinates().get()) {
            handleExplorerMap(itemStack, tooltip);
        }

        if (config.showSuspiciousStewEffect().get()) {
            handleSuspiciousStewEffect(itemStack, tooltip);
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

    private void handleSuspiciousStewEffect(ItemStack itemStack, List<Component> tooltip) {
        List<PotionEffect> stewEffects = itemQuery.getStewEffect(itemStack);
        if (stewEffects.isEmpty()) return;

        for (PotionEffect stewEffect : stewEffects) {
            String name = Laby.labyAPI().minecraft().getTranslation(stewEffect.getTranslationKey());
            String duration = TimeUtil.formatTickDuration(stewEffect.getDuration());
            if (stewEffect.isInfiniteDuration()) {
                duration = I18n.translate("advancedtooltip.tooltip.potion_effect.duration_infinity");
            }

            tooltip(tooltip, false, I18n.translate("advancedtooltip.tooltip.potion_effect.display", name, duration));
        }
    }

    private void tooltip(List<Component> tooltip, String key, Object... value) {
        tooltip(tooltip, true, key, value);
    }

    private void tooltip(List<Component> tooltip, boolean useTranslation, String key, Object... value) {
        TextColor color = TextColor.color(config.tooltipColor().get().get());
        String text = useTranslation ? I18n.translate("advancedtooltip.tooltip." + key, value) : key;
        tooltip.add(Component.text(text, color));
    }


}
