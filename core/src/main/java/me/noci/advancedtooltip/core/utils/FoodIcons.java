package me.noci.advancedtooltip.core.utils;

import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import me.noci.advancedtooltip.core.config.AdvancedTooltipConfiguration;
import me.noci.advancedtooltip.core.config.SaturationType;
import me.noci.advancedtooltip.core.referenceable.ItemQuery;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.util.collection.Lists;

import java.util.List;
import java.util.function.Function;

public enum FoodIcons {

    FULL_FOOD(52, 27),
    HALF_FOOD(61, 27),
    FULL_SATURATION(70, 27),
    HALF_SATURATION(79, 27);

    private final Icon icon;

    FoodIcons(int x, int y) {
        ResourceLocation location = Laby.labyAPI().minecraft().textures().iconsTexture();
        icon = Icon.sprite(location, x, y, 9, 9, 256, 256);
    }

    public void draw(Stack stack, int x, int y, int size) {
        icon.render(stack, x, y, size);
    }

    public static void drawRow(List<FoodIcons> icons, Stack stack, int x, int y, int iconSize, int iconSpacing) {
        int cx = x;
        for (FoodIcons icon : icons) {
            icon.draw(stack, cx, y, iconSize);
            cx += iconSize + iconSpacing;
        }
    }

    public static int getWidth(List<FoodIcons> icons, int iconSize, int iconSpacing) {
        int width = 0;
        for (FoodIcons icon : icons) {
            width += iconSize + iconSpacing;
        }
        return width;
    }

    public static <T> List<T> getIcons(ItemStack itemStack, Function<List<FoodIcons>, T> convert, Class<T> type) {
        if (AdvancedTooltipAddon.getInstance().configuration().developerSettings().showNBTData())
            return List.of();

        List<FoodIcons> saturationIcons = getSaturationIcons(itemStack);
        List<FoodIcons> foodLevelIcons = getFoodLevelIcons(itemStack);

        List<T> icons = Lists.newArrayList();
        if (!saturationIcons.isEmpty()) {
            icons.add(convert.apply(saturationIcons));
        }

        if (!foodLevelIcons.isEmpty()) {
            icons.add(convert.apply(foodLevelIcons));
        }

        return icons;
    }

    private static List<FoodIcons> getSaturationIcons(ItemStack itemStack) {
        if (!itemStack.isFood()) return List.of();

        AdvancedTooltipAddon addon = AdvancedTooltipAddon.getInstance();
        AdvancedTooltipConfiguration configuration = addon.configuration();
        SaturationType saturationType = configuration.saturationLevel().get();
        ItemQuery itemQuery = addon.getItemQuery();

        if (saturationType == SaturationType.HIDDEN) {
            return List.of();
        }

        List<FoodIcons> icons = Lists.newArrayList();
        float saturationIncrement = (saturationType == SaturationType.MAX_SATURATION) ?
                itemQuery.getSaturationIncrement(itemStack).orElse((float) 0) : itemQuery.getAddedSaturation(itemStack).orElse((float) 0);

        while (saturationIncrement >= 2) {
            saturationIncrement -= 2;
            icons.add(FoodIcons.FULL_SATURATION);
        }

        if (saturationIncrement > 0) {
            icons.add(FoodIcons.HALF_SATURATION);
        }

        return icons;
    }

    private static List<FoodIcons> getFoodLevelIcons(ItemStack itemStack) {
        if (!itemStack.isFood()) return List.of();

        AdvancedTooltipAddon addon = AdvancedTooltipAddon.getInstance();
        AdvancedTooltipConfiguration configuration = addon.configuration();
        ItemQuery itemQuery = addon.getItemQuery();
        boolean showFoodLevel = configuration.foodLevel().get();

        if (!showFoodLevel) {
            return List.of();
        }

        List<FoodIcons> icons = Lists.newArrayList();
        int foodLevel = itemQuery.getNutrition(itemStack).orElse(0);

        while (foodLevel >= 2) {
            foodLevel -= 2;
            icons.add(FoodIcons.FULL_FOOD);
        }

        if (foodLevel > 0) {
            icons.add(FoodIcons.HALF_FOOD);
        }

        return icons;
    }

}
