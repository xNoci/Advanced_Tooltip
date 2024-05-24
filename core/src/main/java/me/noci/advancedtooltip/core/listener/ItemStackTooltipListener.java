package me.noci.advancedtooltip.core.listener;

import me.noci.advancedtooltip.core.TooltipAddon;
import me.noci.advancedtooltip.core.config.DurabilityType;
import me.noci.advancedtooltip.core.config.TooltipConfiguration;
import me.noci.advancedtooltip.core.config.text.DurationTextTooltipConfig;
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
import java.util.function.Function;

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
        TooltipRenderer renderer = (color, useTranslation, key, values) -> {
            String text = useTranslation ? I18n.translate("advancedtooltip.tooltip." + key, values) : key;
            event.getTooltipLines().add(Component.text(text, color));
        };

        var displayComponent = config.displayComponent();
        if (displayComponent.displayItemData()) {
            handleShowNbtData(renderer, itemStack, displayComponent.textColor());
            return;
        }

        var durability = config.itemDurability();
        if (durability.enabled() && event.type() != ItemStackTooltipEvent.TooltipType.ADVANCED) {
            handleShowDurability(renderer, itemStack, durability::textColor, durability.durabilityType());
        }

        var anvilUses = config.anvilUsages();
        if (anvilUses.enabled()) {
            handleAnvilUses(renderer, itemStack, anvilUses.textColor());
        }

        var discSignalStrength = config.discSignalStrength();
        if (discSignalStrength.enabled()) {
            handleDiscSignalStrength(renderer, itemStack, discSignalStrength.textColor());
        }

        var mapDecoration = config.mapDecoration();
        if (mapDecoration.enabled()) {
            handleExplorerMap(renderer, itemStack, mapDecoration.textColor());
        }

        var suspiciousStewEffect = config.suspiciousStewEffect();
        if (suspiciousStewEffect.enabled() && !event.isCreative()) {
            handleSuspiciousStewEffect(renderer, itemStack, suspiciousStewEffect.textColor());
        }

        var commandBlockCommand = config.commandBlockCommand();
        if (commandBlockCommand.enabled()) {
            handleCommandBlockCommand(renderer, itemStack, commandBlockCommand.textColor());
        }

        boolean miningItem = itemHelper.isMiningTool(itemStack);
        var miningLevel = config.miningLevel();
        if (miningLevel.enabled() && miningItem) {
            itemHelper.miningLevel(itemStack)
                    .ifPresent(level -> renderer.render(miningLevel.textColor(), "mining.level." + level));
        }

        var miningSpeed = config.miningSpeed();
        if (miningSpeed.enabled() && miningItem) {
            itemHelper.miningSpeed(itemStack, miningSpeed.applyEnchantments())
                    .ifPresent(speed -> renderer.render(miningLevel.textColor(), "mining_speed", speed));
        }

        var clockTime = config.clockTime();
        if (clockTime.enabled() && itemHelper.isClock(itemStack)) {
            long time = Laby.references().clientWorld().getDayTime();
            int hours = (int) (time / 1000 + 6) % 24;
            int minutes = (int) (60 * (time % 1000) / 1000);

            String clockFormat;
            if (clockTime.format24Hours()) {
                clockFormat = "%02d:%02d".formatted(hours, minutes);
            } else {
                String suffix = hours >= 12 ? "PM" : "AM";
                hours = hours % 12 == 0 ? 12 : hours % 12;
                clockFormat = "%d:%02d %s".formatted(hours, minutes, suffix);
            }

            renderer.render(clockTime.textColor(), false, clockFormat);
        }

        var burnDuration = config.burnDuration();
        if (burnDuration.enabled() && itemHelper.isFuel(itemStack)) {
            handleDuration(burnDuration, renderer, itemHelper.burnDuration(itemStack));
        }

        var recordDuration = config.recordDuration();
        if (recordDuration.enabled()) {
            handleDuration(recordDuration, renderer, itemHelper.discTickLength(itemStack).orElse(0));
        }

        var signText = config.signText();
        if (signText.enabled()) {
            handleShowSignText(renderer, itemStack, signText.textColor());
        }

        var compassTarget = config.compassTarget();
        if (compassTarget.enabled()) {
            itemHelper.compassTarget(itemStack)
                    .ifPresent(target -> {
                        String key = "compass_target." + (target.correctDimension() ? "valid" : "wrong_target_dimension");
                        renderer.render(compassTarget.textColor(), key, target.x(), target.y(), target.z());
                    });
        }

    }

    private void handleDuration(DurationTextTooltipConfig durationConfig, TooltipRenderer renderer, int duration) {
        if (duration <= 0) return;

        switch (durationConfig.durationUnit()) {
            case TICKS ->
                    renderer.render(durationConfig.textColor(), "duration_unit." + durationConfig.configKey() + ".ticks", duration);
            case SECONDS ->
                    renderer.render(durationConfig.textColor(), "duration_unit." + durationConfig.configKey() + ".seconds", duration);
            case MINUTES -> {
                int seconds = (duration / 20) % 60;
                int minutes = duration / 1200;

                if (minutes > 0 && seconds > 0) {
                    renderer.render(durationConfig.textColor(), "duration_unit." + durationConfig.configKey() + ".minutes_seconds", minutes, seconds);
                } else if (minutes == 0) {
                    renderer.render(durationConfig.textColor(), "duration_unit." + durationConfig.configKey() + ".seconds", seconds);
                } else {
                    renderer.render(durationConfig.textColor(), "duration_unit." + durationConfig.configKey() + ".minutes", minutes);
                }
            }
        }
    }

    private void handleShowNbtData(TooltipRenderer renderer, ItemStack itemStack, TextColor color) {
        boolean withNbtArrayData = config.displayComponent().printWithNbtArrayData().isPressed();
        componentHelper.displayItemData(itemStack, withNbtArrayData)
                .ifPresentOrElse(data -> {
                    renderer.render(color, false, "");
                    for (String s : data.split("\n")) {
                        renderer.render(color, false, s);
                    }
                }, () -> renderer.render(color, "no_nbt_data"));
    }

    private void handleShowDurability(TooltipRenderer renderer, ItemStack itemStack, Function<Float, TextColor> textColor, DurabilityType durabilityType) {
        if (itemStack.getMaximumDamage() <= 0) return;
        if (componentHelper.unbreakable(itemStack)) return;

        int currentDamage = itemStack.getMaximumDamage() - itemStack.getCurrentDamageValue();
        int maxDamage = itemStack.getMaximumDamage();
        float durability = (float) currentDamage / maxDamage;

        String percentage = PERCENTAGE_FORMAT.format(durability * 100);
        TextColor color = textColor.apply(durability);

        switch (durabilityType) {
            case VANILLA -> renderer.render(color, "durability.type.vanilla", currentDamage, maxDamage);
            case PERCENTAGE -> renderer.render(color, "durability.type.percentage", percentage);
            case COMBINED -> renderer.render(color, "durability.type.combined", currentDamage, maxDamage, percentage);
        }
    }

    private void handleAnvilUses(TooltipRenderer renderer, ItemStack itemStack, TextColor color) {
        int usages = componentHelper.anvilUsages(itemStack).orElse(0);
        if (usages == 0) return;
        renderer.render(color, "anvil_usages", usages);
    }

    private void handleDiscSignalStrength(TooltipRenderer renderer, ItemStack itemStack, TextColor color) {
        itemHelper.discSignalStrengt(itemStack)
                .ifPresent(strength -> renderer.render(color, "disc_signal_strength", strength));
    }

    private void handleExplorerMap(TooltipRenderer renderer, ItemStack itemStack, TextColor color) {
        componentHelper.mapDecorations(itemStack)
                .ifPresent(mapLocations -> mapLocations
                        .stream()
                        .filter(mapDecoration -> mapDecoration.type().showInTooltip())
                        .forEach(mapLocation -> {
                            String translationKey = mapLocation.type().translationKey();
                            double x = mapLocation.x();
                            double z = mapLocation.z();
                            renderer.render(color, translationKey, x, z);
                        }));
    }

    private void handleSuspiciousStewEffect(TooltipRenderer renderer, ItemStack itemStack, TextColor color) {
        foodItems.stewEffect(itemStack)
                .ifPresent(stewEffects -> {
                    for (PotionEffect stewEffect : stewEffects) {
                        String name = Laby.labyAPI().minecraft().getTranslation(stewEffect.getTranslationKey());
                        String duration = TimeUtil.formatTickDuration(stewEffect.getDuration());
                        if (stewEffect.isInfiniteDuration()) {
                            duration = I18n.translate("advancedtooltip.tooltip.potion_effect.duration_infinity");
                        }

                        renderer.render(color, false, I18n.translate("advancedtooltip.tooltip.potion_effect.display", name, duration));
                    }
                });
    }

    private void handleCommandBlockCommand(TooltipRenderer renderer, ItemStack itemStack, TextColor color) {
        componentHelper.commandBlockCommand(itemStack)
                .ifPresent(command -> {
                    if (command.isEmpty()) {
                        renderer.render(color, "command_block_no_command");
                    } else {
                        renderer.render(color, "command_block_command", command);
                    }
                });
    }

    private void handleShowSignText(TooltipRenderer renderer, ItemStack itemStack, TextColor color) {
        componentHelper.signText(itemStack)
                .ifPresent(signText -> {
                    boolean hasFrontText = signText.hasFrontText();
                    boolean hasBackText = signText.hasBackText();

                    if (!hasFrontText && !hasBackText) return;
                    if (hasFrontText) {
                        renderer.render(color, "sign_text.front_text");
                        for (int i = 0; i < signText.frontText().length; i++) {
                            renderer.render(color, "sign_text.line", signText.frontText()[i]);
                        }
                        if (hasBackText) {
                            renderer.render(color, false, "");
                        }
                    }

                    if (hasBackText) {
                        renderer.render(color, "sign_text.back_text");
                        for (int i = 0; i < signText.backText().length; i++) {
                            renderer.render(color, "sign_text.line", signText.backText()[i]);
                        }
                    }
                });
    }

    @FunctionalInterface
    private interface TooltipRenderer {

        default void render(TextColor color, String key, Object... values) {
            render(color, true, key, values);
        }

        void render(TextColor color, boolean useTranslation, String key, Object... values);

    }


}
