package me.noci.advancedtooltip.core.utils;

import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import me.noci.advancedtooltip.core.config.AdvancedTooltipConfiguration;
import me.noci.advancedtooltip.core.config.SaturationType;
import me.noci.advancedtooltip.core.referenceable.ItemQuery;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.util.collection.Lists;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public record IconQuery(FoodIcon full_icon, FoodIcon half_icon, ShowFunction showFunction,
                        LevelFunction levelFunction) {

    private static final IconQuery FOOD_QUERY = new IconQuery(FoodIcon.FULL_FOOD, FoodIcon.HALF_FOOD, AdvancedTooltipConfiguration::showFoodLevel, (c, iq, is) -> iq.getNutrition(is));
    private static final IconQuery SATURATION_QUERY = new IconQuery(FoodIcon.FULL_SATURATION, FoodIcon.HALF_SATURATION, AdvancedTooltipConfiguration::showSaturationLevel, (c, iq, is) -> (c.saturationType() == SaturationType.MAX_SATURATION) ? iq.getSaturationIncrement(is) : iq.getAddedSaturation(is));

    public static <T> List<T> getIcons(ItemStack itemStack, Function<List<FoodIcon>, T> convert, Class<T> type) {
        AdvancedTooltipAddon addon = AdvancedTooltipAddon.getInstance();
        AdvancedTooltipConfiguration configuration = addon.configuration();
        if (configuration.developerSettings().showNBTData())
            return List.of();

        List<T> icons = Lists.newArrayList();

        SATURATION_QUERY.apply(itemStack, icons, convert, type);
        FOOD_QUERY.apply(itemStack, icons, convert, type);

        return icons;
    }

    private boolean shouldShow(AdvancedTooltipConfiguration configuration) {
        return showFunction.apply(configuration);
    }

    private float getLevel(AdvancedTooltipConfiguration configuration, ItemQuery itemQuery, ItemStack itemStack) {
        return levelFunction.apply(configuration, itemQuery, itemStack).map(Number::floatValue).orElse(0F);
    }

    private <T> void apply(ItemStack itemStack, List<T> icons, Function<List<FoodIcon>, T> convert, Class<T> type) {
        if (!itemStack.isFood()) return;
        AdvancedTooltipAddon addon = AdvancedTooltipAddon.getInstance();
        AdvancedTooltipConfiguration configuration = addon.configuration();
        ItemQuery itemQuery = addon.getItemQuery();

        if (!shouldShow(configuration)) return;
        List<FoodIcon> temp = Lists.newArrayList();

        float level = getLevel(configuration, itemQuery, itemStack);
        while (level >= 2) {
            level -= 2;
            temp.add(full_icon);
        }

        if (level > 0) {
            temp.add(half_icon);
        }

        if (temp.isEmpty()) return;
        icons.add(convert.apply(temp));
    }

    @FunctionalInterface
    private interface LevelFunction {
        Optional<? extends Number> apply(AdvancedTooltipConfiguration configuration, ItemQuery itemQuery, ItemStack itemStack);
    }

    @FunctionalInterface
    private interface ShowFunction {
        boolean apply(AdvancedTooltipConfiguration configuration);
    }

}
