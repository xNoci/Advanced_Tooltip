package me.noci.advancedtooltip.core.listener;

import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import me.noci.advancedtooltip.core.config.AdvancedTooltipConfiguration;
import me.noci.advancedtooltip.core.referenceable.ItemQuery;
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
import java.util.Optional;

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
            Optional<String> nbt = itemQuery.getItemNBTData(itemStack, config.developerSettings().printWithArrayData().get().isPressed());
            if (nbt.isEmpty()) {
                tooltip(tooltip, "no_nbt_data");
                return;
            }

            tooltip(tooltip, false, "");
            for (String s : nbt.get().split("\n")) {
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
        Optional<Integer> usages = itemQuery.getAnvilUsages(itemStack);
        if (usages.isEmpty()) return;
        tooltip(tooltip, "anvil_usages", usages.get());
    }

    private void handleDiscSignalStrength(ItemStack itemStack, List<Component> tooltip) {
        Optional<Integer> strength = itemQuery.getDiscSignalStrengt(itemStack);
        if (strength.isEmpty()) return;
        tooltip(tooltip, "disc_signal_strength", strength.get());
    }

    private void handleExplorerMap(ItemStack itemStack, List<Component> tooltip) {
        Optional<MapLocation> mapLocation = itemQuery.getExplorerMapLocation(itemStack);
        if (mapLocation.isEmpty()) return;
        tooltip(tooltip, "explorer_map." + mapLocation.get().getType().name().toLowerCase(), mapLocation.get().getX(), mapLocation.get().getZ());
    }

    private void handleSuspiciousStewEffect(ItemStack itemStack, List<Component> tooltip) {
        Optional<List<PotionEffect>> stewEffects = itemQuery.getStewEffect(itemStack);
        if (stewEffects.isEmpty()) return;

        for (PotionEffect stewEffect : stewEffects.get()) {
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