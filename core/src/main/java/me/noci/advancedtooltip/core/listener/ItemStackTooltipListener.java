package me.noci.advancedtooltip.core.listener;

import me.noci.advancedtooltip.core.TooltipAddon;
import me.noci.advancedtooltip.core.config.DurabilityType;
import me.noci.advancedtooltip.core.config.TooltipConfiguration;
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

import java.text.DecimalFormat;
import java.util.List;

public class ItemStackTooltipListener {

    private static final DecimalFormat PERCENTAGE_FORMAT = new DecimalFormat("0.##");

    private final TooltipConfiguration config;
    private final FoodItems foodItems;
    private final ItemHelper itemHelper;
    private final ComponentHelper componentHelper;

    public ItemStackTooltipListener(TooltipAddon addon, FoodItems foodItems, ItemHelper itemHelper, ComponentHelper componentHelper) {
        this.config = addon.configuration();
        this.foodItems = foodItems;
        this.itemHelper = itemHelper;
        this.componentHelper = componentHelper;
    }

    @Subscribe
    public void onToolTip(ItemStackTooltipEvent event) {
        ItemStack itemStack = event.itemStack();
        List<Component> tooltip = event.getTooltipLines();

        var displayComponent = config.displayComponent();
        if (displayComponent.displayItemData()) {
            handleShowNbtData(itemStack, tooltip, displayComponent.textColor());
            return;
        }

        var durability = config.itemDurability();
        if (durability.enabled() && event.type() != ItemStackTooltipEvent.TooltipType.ADVANCED) {
            handleShowDurability(itemStack, tooltip, durability.textColor(), durability.durabilityType());
        }

        var anvilUses = config.anvilUsages();
        if (anvilUses.enabled()) {
            handleAnvilUses(itemStack, tooltip, anvilUses.textColor());
        }

        var discSignalStrength = config.discSignalStrength();
        if (discSignalStrength.enabled()) {
            handleDiscSignalStrength(itemStack, tooltip, discSignalStrength.textColor());
        }

        var mapDecoration = config.mapDecoration();
        if (mapDecoration.enabled()) {
            handleExplorerMap(itemStack, tooltip, mapDecoration.textColor());
        }

        var suspiciousStewEffect = config.suspiciousStewEffect();
        if (suspiciousStewEffect.enabled() && !event.isCreative()) {
            handleSuspiciousStewEffect(itemStack, tooltip, suspiciousStewEffect.textColor());
        }

        var commandBlockCommand = config.commandBlockCommand();
        if (commandBlockCommand.enabled()) {
            handleCommandBlockCommand(itemStack, tooltip, commandBlockCommand.textColor());
        }

        boolean miningItem = itemHelper.isMiningTool(itemStack);
        var miningLevel = config.miningLevel();
        if (miningLevel.enabled() && miningItem) {
            itemHelper.miningLevel(itemStack)
                    .ifPresent(level -> tooltip(tooltip, miningLevel.textColor(), "mining.level." + level));
        }

        var miningSpeed = config.miningSpeed();
        if (miningSpeed.enabled() && miningItem) {
            itemHelper.miningSpeed(itemStack, miningSpeed.applyEnchantments())
                    .ifPresent(speed -> tooltip(tooltip, miningLevel.textColor(), "mining_speed", speed));
        }

        var signText = config.signText();
        if (signText.enabled()) {
            handleShowSignText(itemStack, tooltip, signText.textColor());
        }

    }

    private void handleShowNbtData(ItemStack itemStack, List<Component> tooltip, TextColor color) {
        boolean withNbtArrayData = config.displayComponent().printWithNbtArrayData().isPressed();
        componentHelper.displayItemData(itemStack, withNbtArrayData)
                .ifPresentOrElse(data -> {
                    tooltip(tooltip, color, false, "");
                    for (String s : data.split("\n")) {
                        tooltip(tooltip, color, false, s);
                    }
                }, () -> tooltip(tooltip, color, "no_nbt_data"));
    }

    private void handleShowDurability(ItemStack itemStack, List<Component> tooltip, TextColor color, DurabilityType durabilityType) {
        if (itemStack.getMaximumDamage() <= 0) return;
        if (componentHelper.unbreakable(itemStack)) return;

        int currentDamage = itemStack.getMaximumDamage() - itemStack.getCurrentDamageValue();
        int maxDamage = itemStack.getMaximumDamage();
        String percentage = PERCENTAGE_FORMAT.format(((float) currentDamage / maxDamage) * 100);

        switch (durabilityType) {
            case VANILLA -> tooltip(tooltip, color, "durability.type.vanilla", currentDamage, maxDamage);
            case PERCENTAGE -> tooltip(tooltip, color, "durability.type.percentage", percentage);
            case COMBINED -> tooltip(tooltip, color, "durability.type.combined", currentDamage, maxDamage, percentage);
        }
    }

    private void handleAnvilUses(ItemStack itemStack, List<Component> tooltip, TextColor color) {
        int usages = componentHelper.anvilUsages(itemStack).orElse(0);
        if (usages == 0) return;
        tooltip(tooltip, color, "anvil_usages", usages);
    }

    private void handleDiscSignalStrength(ItemStack itemStack, List<Component> tooltip, TextColor color) {
        itemHelper.discSignalStrengt(itemStack)
                .ifPresent(strength -> tooltip(tooltip, color, "disc_signal_strength", strength));
    }

    private void handleExplorerMap(ItemStack itemStack, List<Component> tooltip, TextColor color) {
        componentHelper.mapDecorations(itemStack)
                .ifPresent(mapLocations -> mapLocations
                        .stream()
                        .filter(mapDecoration -> mapDecoration.type().showInTooltip())
                        .forEach(mapLocation -> {
                            String translationKey = mapLocation.type().translationKey();
                            double x = mapLocation.x();
                            double z = mapLocation.z();
                            tooltip(tooltip, color, translationKey, x, z);
                        }));
    }

    private void handleSuspiciousStewEffect(ItemStack itemStack, List<Component> tooltip, TextColor color) {
        foodItems.stewEffect(itemStack)
                .ifPresent(stewEffects -> {
                    for (PotionEffect stewEffect : stewEffects) {
                        String name = Laby.labyAPI().minecraft().getTranslation(stewEffect.getTranslationKey());
                        String duration = TimeUtil.formatTickDuration(stewEffect.getDuration());
                        if (stewEffect.isInfiniteDuration()) {
                            duration = I18n.translate("advancedtooltip.tooltip.potion_effect.duration_infinity");
                        }

                        tooltip(tooltip, color, false, I18n.translate("advancedtooltip.tooltip.potion_effect.display", name, duration));
                    }
                });
    }

    private void handleCommandBlockCommand(ItemStack itemStack, List<Component> tooltip, TextColor color) {
        componentHelper.commandBlockCommand(itemStack)
                .ifPresent(command -> {
                    if (command.isEmpty()) {
                        tooltip(tooltip, color, "command_block_no_command");
                    } else {
                        tooltip(tooltip, color, "command_block_command", command);
                    }
                });
    }

    private void handleShowSignText(ItemStack itemStack, List<Component> tooltip, TextColor color) {
        componentHelper.signText(itemStack)
                .ifPresent(signText -> {
                    boolean hasFrontText = signText.hasFrontText();
                    boolean hasBackText = signText.hasBackText();

                    if (!hasFrontText && !hasBackText) return;
                    if (hasFrontText) {
                        tooltip(tooltip, color, "sign_text.front_text");
                        for (int i = 0; i < signText.frontText().length; i++) {
                            tooltip(tooltip, color, "sign_text.line", signText.frontText()[i]);
                        }
                        if (hasBackText) tooltip(tooltip, color, false, "");
                    }

                    if (hasBackText) {
                        tooltip(tooltip, color, "sign_text.back_text");
                        for (int i = 0; i < signText.backText().length; i++) {
                            tooltip(tooltip, color, "sign_text.line", signText.backText()[i]);
                        }
                    }
                });
    }


    private void tooltip(List<Component> tooltip, TextColor color, String key, Object... value) {
        tooltip(tooltip, color, true, key, value);
    }

    private void tooltip(List<Component> tooltip, TextColor color, boolean useTranslation, String key, Object... value) {
        String text = useTranslation ? I18n.translate("advancedtooltip.tooltip." + key, value) : key;
        tooltip.add(Component.text(text, color));
    }


}
