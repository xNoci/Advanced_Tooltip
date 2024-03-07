package me.noci.advancedtooltip.core.icons;

import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import me.noci.advancedtooltip.core.config.AdvancedTooltipConfiguration;
import me.noci.advancedtooltip.core.config.SaturationType;
import me.noci.advancedtooltip.core.referenceable.items.FoodItems;
import me.noci.advancedtooltip.core.referenceable.items.ItemQuery;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.util.collection.Lists;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public record IconQuery(TooltipIcon full_icon, TooltipIcon half_icon, ItemValidator itemValidator,
                        ShowFunction showFunction,
                        LevelFunction levelFunction) {

    private static final List<IconQuery> iconQueries = Lists.newArrayList();

    static {
        iconQueries.add(new IconQuery(TooltipIcon.FULL_FOOD, TooltipIcon.HALF_FOOD, ItemValidator.IS_FOOD, ShowFunction.NUTRITION, LevelFunction.NUTRITION));
        iconQueries.add(new IconQuery(TooltipIcon.FULL_SATURATION, TooltipIcon.HALF_SATURATION, ItemValidator.IS_FOOD, ShowFunction.SATURATION, LevelFunction.SATURATION));
        iconQueries.add(new IconQuery(TooltipIcon.FULL_ARMOR, TooltipIcon.HALF_ARMOR, ItemValidator.IS_ARMOR, ShowFunction.ARMOR_BARS, LevelFunction.ARMOR_BARS));
    }

    public static <T extends ClientIconComponent> List<T> getIcons(ItemStack itemStack, Function<List<TooltipIcon>, T> convert) {
        AdvancedTooltipAddon addon = AdvancedTooltipAddon.getInstance();
        AdvancedTooltipConfiguration config = addon.configuration();
        if (config.developerSettings().isDisplayItemData()) return List.of();

        List<T> icons = Lists.newArrayList();

        for (IconQuery iconQuery : iconQueries) {
            iconQuery.apply(icons, itemStack, convert);
        }

        if (!icons.isEmpty()) {
            icons.get(0).setFirstComponent();
            icons.get(icons.size() - 1).setLastComponent();
        }

        return icons;
    }

    private <T> void apply(List<T> icons, ItemStack itemStack, Function<List<TooltipIcon>, T> convert) {
        AdvancedTooltipAddon addon = AdvancedTooltipAddon.getInstance();
        AdvancedTooltipConfiguration configuration = addon.configuration();
        FoodItems foodItems = addon.getFoodItems();
        ItemQuery itemQuery = addon.getItemQuery();

        if (!itemValidator.isValid(itemQuery, itemStack)) return;
        if (!showFunction.shouldShow(configuration)) return;
        List<TooltipIcon> temp = Lists.newArrayList();

        float level = levelFunction.get(configuration, foodItems, itemQuery, itemStack);
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
    private interface ItemValidator {
        ItemValidator IS_FOOD = (itemQuery, itemStack) -> itemStack.isFood();
        ItemValidator IS_ARMOR = ItemQuery::isArmor;

        boolean isValid(ItemQuery itemQuery, ItemStack itemStack);
    }

    @FunctionalInterface
    private interface LevelFunction {
        LevelFunction NUTRITION = (c, fi, iq, is) -> fi.nutrition(is);
        LevelFunction SATURATION = (c, fi, iq, is) -> (c.saturationType() == SaturationType.MAX_SATURATION) ? fi.saturationIncrement(is) : fi.addedSaturation(is);
        LevelFunction ARMOR_BARS = (c, fi, iq, is) -> iq.getArmorBars(is);

        Optional<? extends Number> apply(AdvancedTooltipConfiguration config, FoodItems foodItems, ItemQuery itemQuery, ItemStack itemStack);

        default float get(AdvancedTooltipConfiguration config, FoodItems foodItems, ItemQuery itemQuery, ItemStack itemStack) {
            return apply(config, foodItems, itemQuery, itemStack).map(Number::floatValue).orElse(0F);
        }
    }

    @FunctionalInterface
    private interface ShowFunction {

        ShowFunction NUTRITION = AdvancedTooltipConfiguration::showFoodLevel;
        ShowFunction SATURATION = AdvancedTooltipConfiguration::showSaturationLevel;
        ShowFunction ARMOR_BARS = AdvancedTooltipConfiguration::showArmorBarIcons;

        boolean shouldShow(AdvancedTooltipConfiguration config);
    }

}
