package me.noci.advancedtooltip.core.icons;

import me.noci.advancedtooltip.core.TooltipAddon;
import me.noci.advancedtooltip.core.config.SaturationType;
import me.noci.advancedtooltip.core.config.TooltipConfiguration;
import me.noci.advancedtooltip.core.referenceable.items.FoodItems;
import me.noci.advancedtooltip.core.referenceable.items.ItemHelper;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.util.collection.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public record IconQuery(TooltipIcon full_icon, TooltipIcon half_icon, ItemValidator itemValidator,
                        ShowFunction showFunction, LevelFunction levelFunction) {

    private static final List<IconQuery> iconQueries = Lists.newArrayList();

    static {
        iconQueries.add(new IconQuery(TooltipIcon.FULL_FOOD, TooltipIcon.HALF_FOOD, ItemValidator.IS_FOOD, ShowFunction.NUTRITION, LevelFunction.NUTRITION));
        iconQueries.add(new IconQuery(TooltipIcon.FULL_SATURATION, TooltipIcon.HALF_SATURATION, ItemValidator.IS_FOOD, ShowFunction.SATURATION, LevelFunction.SATURATION));
        iconQueries.add(new IconQuery(TooltipIcon.FULL_ARMOR, TooltipIcon.HALF_ARMOR, ItemValidator.IS_ARMOR, ShowFunction.ARMOR_BARS, LevelFunction.ARMOR_BARS));
    }

    public static <T extends IconComponent> List<T> iconComponents(ItemStack itemStack, VersionedIconComponentMapper<T> mapper) {
        TooltipAddon addon = TooltipAddon.get();
        TooltipConfiguration config = addon.configuration();
        if (config.displayComponent().displayItemData()) return new ArrayList<>();

        List<T> iconComponents = iconQueries.stream()
                .map(iconQuery -> iconQuery.itemIcons(itemStack, mapper))
                .flatMap(Optional::stream)
                .collect(Collectors.toCollection(ArrayList::new));

        if (!iconComponents.isEmpty()) {
            iconComponents.getFirst().setFirstComponent();
            iconComponents.getLast().setLastComponent();
        }

        return iconComponents;
    }

    private <T extends IconComponent> Optional<T> itemIcons(ItemStack itemStack, VersionedIconComponentMapper<T> mapper) {
        TooltipConfiguration config = TooltipAddon.get().configuration();
        FoodItems foodItems = TooltipAddon.foodItems();
        ItemHelper itemHelper = TooltipAddon.itemHelper();

        if (!itemValidator.isValid(itemHelper, itemStack) || !showFunction.shouldShow(config)) return Optional.empty();

        List<TooltipIcon> itemIcons = Lists.newArrayList();
        float level = Math.max(0, levelFunction.get(config, foodItems, itemHelper, itemStack));
        while (level >= 2) {
            level -= 2;
            itemIcons.add(full_icon);
        }

        if (level > 0) {
            itemIcons.add(half_icon);
        }

        if (itemIcons.isEmpty()) return Optional.empty();

        T versionIconComponent = mapper.apply(itemIcons);
        return Optional.of(versionIconComponent);
    }

    @FunctionalInterface
    private interface ItemValidator {
        ItemValidator IS_FOOD = (itemQuery, itemStack) -> itemStack.isFood();
        ItemValidator IS_ARMOR = ItemHelper::isArmor;

        boolean isValid(ItemHelper itemQuery, ItemStack itemStack);
    }

    @FunctionalInterface
    private interface LevelFunction {
        LevelFunction NUTRITION = (c, fi, ih, is) -> fi.nutrition(is);
        LevelFunction SATURATION = (c, fi, ih, is) -> (c.saturationIcons().saturationType() == SaturationType.MAX_SATURATION) ? fi.saturationIncrement(is) : fi.addedSaturation(is);
        LevelFunction ARMOR_BARS = (c, fi, ih, is) -> ih.armorBars(is);

        float get(TooltipConfiguration config, FoodItems foodItems, ItemHelper itemHelper, ItemStack itemStack);
    }

    @FunctionalInterface
    private interface ShowFunction {

        ShowFunction NUTRITION = config -> config.nutritionIcons().enabled();
        ShowFunction SATURATION = config -> config.saturationIcons().enabled();
        ShowFunction ARMOR_BARS = config -> config.armorIcons().enabled();

        boolean shouldShow(TooltipConfiguration config);
    }

}
