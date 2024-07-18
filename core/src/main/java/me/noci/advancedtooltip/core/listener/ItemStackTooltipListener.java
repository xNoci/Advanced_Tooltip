package me.noci.advancedtooltip.core.listener;

import me.noci.advancedtooltip.core.TooltipAddon;
import me.noci.advancedtooltip.core.config.TooltipConfiguration;
import me.noci.advancedtooltip.core.config.text.DurationTextTooltipConfig;
import me.noci.advancedtooltip.core.referenceable.TickManager;
import me.noci.advancedtooltip.core.referenceable.items.ComponentHelper;
import me.noci.advancedtooltip.core.referenceable.items.FoodItems;
import me.noci.advancedtooltip.core.referenceable.items.ItemHelper;
import me.noci.advancedtooltip.core.utils.CompassTarget;
import me.noci.advancedtooltip.core.utils.MapDecoration;
import me.noci.advancedtooltip.core.utils.SignText;
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
    private final TickManager tickManager;

    public ItemStackTooltipListener(TooltipAddon addon, FoodItems foodItems, ItemHelper itemHelper, ComponentHelper componentHelper, TickManager tickManager) {
        this.config = addon.configuration();
        this.foodItems = foodItems;
        this.itemHelper = itemHelper;
        this.componentHelper = componentHelper;
        this.tickManager = tickManager;
    }

    @Subscribe
    public void onToolTip(ItemStackTooltipEvent event) {
        //TODO Setting to disable when advanced tooltips are on
        ItemStack itemStack = event.itemStack();
        TooltipRenderer renderer = (color, useTranslation, key, values) -> {
            String text = useTranslation ? I18n.translate("advancedtooltip.tooltip." + key, values) : key;
            event.getTooltipLines().add(Component.text(text, color));
        };

        var displayComponent = config.displayComponent();
        if (displayComponent.displayItemData()) {
            TextColor color = displayComponent.textColor();
            boolean withNbtArrayData = config.displayComponent().printWithNbtArrayData().isPressed();
            String data = componentHelper.displayItemData(itemStack, withNbtArrayData);

            if (data == null) {
                renderer.render(color, "no_nbt_data");
                return;
            }

            renderer.render(color, false, "");
            for (String s : data.split("\n")) {
                renderer.render(color, false, s);
            }
            return;
        }

        var durabilityOption = config.itemDurability();
        if (durabilityOption.enabled()) {
            if (itemStack.getMaximumDamage() > 0 && !componentHelper.unbreakable(itemStack)) {
                int currentDamage = itemStack.getMaximumDamage() - itemStack.getCurrentDamageValue();
                int maxDamage = itemStack.getMaximumDamage();
                float durability = (float) currentDamage / maxDamage;

                String percentage = PERCENTAGE_FORMAT.format(durability * 100);
                TextColor color = durabilityOption.textColor(durability);

                switch (durabilityOption.durabilityType()) {
                    case VANILLA -> renderer.render(color, "durability.type.vanilla", currentDamage, maxDamage);
                    case PERCENTAGE -> renderer.render(color, "durability.type.percentage", percentage);
                    case COMBINED ->
                            renderer.render(color, "durability.type.combined", currentDamage, maxDamage, percentage);
                }
            }

        }

        var anvilUses = config.anvilUsages();
        if (anvilUses.enabled()) {
            int usages = componentHelper.anvilUsages(itemStack);
            if (usages > 0) {
                renderer.render(anvilUses.textColor(), "anvil_usages", usages);
            }
        }

        var discSignalStrength = config.discSignalStrength();
        if (discSignalStrength.enabled()) {
            int signalStrength = itemHelper.discSignalStrengt(itemStack);
            if (signalStrength > 0) {
                renderer.render(discSignalStrength.textColor(), "disc_signal_strength", signalStrength);
            }
        }

        var mapDecoration = config.mapDecoration();
        if (mapDecoration.enabled()) {
            List<MapDecoration> decorations = componentHelper.mapDecorations(itemStack);

            if (decorations != null) {
                TextColor color = mapDecoration.textColor();

                for (MapDecoration decoration : decorations) {
                    if (!decoration.type().showInTooltip()) continue;
                    String translationKey = decoration.type().translationKey();
                    renderer.render(color, translationKey, decoration.x(), decoration.z());
                }
            }

        }

        var suspiciousStewEffect = config.suspiciousStewEffect();
        if (suspiciousStewEffect.enabled() && !event.isCreative()) {
            List<PotionEffect> effects = foodItems.stewEffect(itemStack);
            if (effects != null) {
                TextColor color = suspiciousStewEffect.textColor();
                effects.forEach(potionEffect -> {
                    String name = Laby.labyAPI().minecraft().getTranslation(potionEffect.getTranslationKey());
                    String duration = TimeUtil.formatTickDuration(potionEffect.getDuration());
                    if (potionEffect.isInfiniteDuration()) {
                        duration = I18n.translate("advancedtooltip.tooltip.potion_effect.duration_infinity");
                    }

                    renderer.render(color, false, I18n.translate("advancedtooltip.tooltip.potion_effect.display", name, duration));
                });
            }
        }

        var commandBlockCommand = config.commandBlockCommand();
        if (commandBlockCommand.enabled()) {
            String command = componentHelper.commandBlockCommand(itemStack);
            if (command != null) {
                TextColor color = commandBlockCommand.textColor();

                String key = command.isEmpty() ? "command_block_no_command" : "command_block_command";
                renderer.render(color, key, command);
            }
        }

        boolean miningItem = itemHelper.isMiningTool(itemStack);
        var miningLevel = config.miningLevel();
        if (miningLevel.enabled() && miningItem) {
            int level = itemHelper.miningLevel(itemStack);
            if (level > 0) {
                renderer.render(miningLevel.textColor(), "mining.level." + level);
            }
        }

        var miningSpeed = config.miningSpeed();
        if (miningSpeed.enabled() && miningItem) {
            float speed = itemHelper.miningSpeed(itemStack, miningSpeed.applyEnchantments());
            if (speed > 0) {
                renderer.render(miningSpeed.textColor(), "mining_speed", speed);
            }
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
            handleDuration(burnDuration, renderer, itemHelper.burnDuration(itemStack), true);
        }

        var recordDuration = config.recordDuration();
        if (recordDuration.enabled()) {
            handleDuration(recordDuration, renderer, itemHelper.discTickLength(itemStack), false);
        }

        var signText = config.signText();
        if (signText.enabled()) {
            SignText text = componentHelper.signText(itemStack);
            if (text != null) {
                boolean hasFrontText = text.hasFrontText();
                boolean hasBackText = text.hasBackText();

                TextColor color = signText.textColor();
                String[] frontText = text.frontText();
                String[] backText = text.backText();

                if (hasFrontText) {
                    renderer.render(color, "sign_text.front_text");
                    for (String s : frontText) {
                        renderer.render(color, "sign_text.line", s);
                    }
                    if (hasBackText) {
                        renderer.render(color, false, "");
                    }
                }

                if (hasBackText) {
                    renderer.render(color, "sign_text.back_text");
                    for (String s : backText) {
                        renderer.render(color, "sign_text.line", s);
                    }
                }
            }

        }

        var compassTarget = config.compassTarget();
        if (compassTarget.enabled()) {
            CompassTarget target = itemHelper.compassTarget(itemStack);
            if (target != null) {
                String key = "compass_target." + (target.correctDimension() ? "valid" : "wrong_target_dimension");
                renderer.render(compassTarget.textColor(), key, target.x(), target.y(), target.z());
            }
        }

    }

    private void handleDuration(DurationTextTooltipConfig durationConfig, TooltipRenderer renderer, int ticks, boolean useTickRate) {
        if (ticks <= 0) return;

        switch (durationConfig.durationUnit()) {
            case TICKS ->
                    renderer.render(durationConfig.textColor(), "duration_unit." + durationConfig.configKey() + ".ticks", ticks);
            case SECONDS ->
                    renderer.render(durationConfig.textColor(), "duration_unit." + durationConfig.configKey() + ".seconds", tickManager.seconds(ticks, useTickRate));
            case MINUTES -> {
                int seconds = tickManager.seconds(ticks, useTickRate) % 60;
                int minutes = tickManager.minutes(ticks, useTickRate);

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

    @FunctionalInterface
    private interface TooltipRenderer {

        default void render(TextColor color, String key, Object... values) {
            render(color, true, key, values);
        }

        void render(TextColor color, boolean useTranslation, String key, Object... values);

    }


}
