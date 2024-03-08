package me.noci.advancedtooltip.core.listener;

import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import me.noci.advancedtooltip.core.config.AdvancedTooltipConfiguration;
import me.noci.advancedtooltip.core.referenceable.items.ComponentHelper;
import me.noci.advancedtooltip.core.referenceable.items.FoodItems;
import me.noci.advancedtooltip.core.referenceable.items.ItemHelper;
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
    private final FoodItems foodItems;
    private final ItemHelper itemHelper;
    private final ComponentHelper componentHelper;

    public ItemStackTooltipListener(AdvancedTooltipAddon addon, FoodItems foodItems, ItemHelper itemHelper, ComponentHelper componentHelper) {
        this.config = addon.configuration();
        this.foodItems = foodItems;
        this.itemHelper = itemHelper;
        this.componentHelper = componentHelper;
    }

    @Subscribe
    public void onToolTip(ItemStackTooltipEvent event) {
        ItemStack itemStack = event.itemStack();
        List<Component> tooltip = event.getTooltipLines();

        if (config.developerSettings().isDisplayItemData()) {
            handleShowNbtData(itemStack, tooltip);
            return;
        }

        if (config.showDurability().get() && event.type() != ItemStackTooltipEvent.TooltipType.ADVANCED) {
            handleShowDurability(itemStack, tooltip);
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

        if (config.showSuspiciousStewEffect().get() && !event.isCreative()) {
            handleSuspiciousStewEffect(itemStack, tooltip);
        }

        if (config.showCommandBlockCommand()) {
            handleCommandBlockCommand(itemStack, tooltip);
        }

        if (config.showSignText()) {
            handleShowSignText(itemStack, tooltip);
        }

    }

    private void handleShowNbtData(ItemStack itemStack, List<Component> tooltip) {
        boolean withNbtArrayData = config.developerSettings().printWithNbtArrayData().get().isPressed();
        componentHelper.displayItemData(itemStack, withNbtArrayData)
                .ifPresentOrElse(data -> {
                    tooltip(tooltip, false, "");
                    for (String s : data.split("\n")) {
                        tooltip(tooltip, false, s);
                    }
                }, () -> tooltip(tooltip, "no_nbt_data"));
    }

    private void handleShowDurability(ItemStack itemStack, List<Component> tooltip) {
        if (itemStack.getMaximumDamage() <= 0) return;
        if (componentHelper.unbreakable(itemStack)) return;
        tooltip(tooltip, "durability", itemStack.getMaximumDamage() - itemStack.getCurrentDamageValue(), itemStack.getMaximumDamage());
    }

    private void handleAnvilUses(ItemStack itemStack, List<Component> tooltip) {
        int usages = componentHelper.anvilUsages(itemStack).orElse(0);
        if (usages == 0) return;
        tooltip(tooltip, "anvil_usages", usages);
    }

    private void handleDiscSignalStrength(ItemStack itemStack, List<Component> tooltip) {
        itemHelper.discSignalStrengt(itemStack)
                .ifPresent(strength -> tooltip(tooltip, "disc_signal_strength", strength));
    }

    private void handleExplorerMap(ItemStack itemStack, List<Component> tooltip) {
        componentHelper.mapDecorations(itemStack)
                .ifPresent(mapLocations -> mapLocations
                        .stream()
                        .filter(mapDecoration -> mapDecoration.type().isShowInTooltip())
                        .forEach(mapLocation -> {
                            String translationKey = mapLocation.type().getTranslationKey();
                            double x = mapLocation.x();
                            double z = mapLocation.z();
                            tooltip(tooltip, translationKey, x, z);
                        }));
    }

    private void handleSuspiciousStewEffect(ItemStack itemStack, List<Component> tooltip) {
        foodItems.stewEffect(itemStack)
                .ifPresent(stewEffects -> {
                    for (PotionEffect stewEffect : stewEffects) {
                        String name = Laby.labyAPI().minecraft().getTranslation(stewEffect.getTranslationKey());
                        String duration = TimeUtil.formatTickDuration(stewEffect.getDuration());
                        if (stewEffect.isInfiniteDuration()) {
                            duration = I18n.translate("advancedtooltip.tooltip.potion_effect.duration_infinity");
                        }

                        tooltip(tooltip, false, I18n.translate("advancedtooltip.tooltip.potion_effect.display", name, duration));
                    }
                });
    }

    private void handleCommandBlockCommand(ItemStack itemStack, List<Component> tooltip) {
        componentHelper.commandBlockCommand(itemStack)
                .ifPresent(command -> {
                    if (command.isEmpty()) {
                        tooltip(tooltip, "command_block_no_command");
                    } else {
                        tooltip(tooltip, "command_block_command", command);
                    }
                });
    }

    private void handleShowSignText(ItemStack itemStack, List<Component> tooltip) {
        componentHelper.signText(itemStack)
                .ifPresent(signText -> {
                    boolean hasFrontText = signText.hasFrontText();
                    boolean hasBackText = signText.hasBackText();

                    if (!hasFrontText && !hasBackText) return;
                    if (hasFrontText) {
                        tooltip(tooltip, "sign_text.front_text");
                        for (int i = 0; i < signText.frontText().length; i++) {
                            tooltip(tooltip, "sign_text.line", signText.frontText()[i]);
                        }
                        if (hasBackText) tooltip(tooltip, false, "");
                    }

                    if (hasBackText) {
                        tooltip(tooltip, "sign_text.back_text");
                        for (int i = 0; i < signText.backText().length; i++) {
                            tooltip(tooltip, "sign_text.line", signText.backText()[i]);
                        }
                    }
                });
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
