package me.noci.advancedtooltip.v1_19_4.util;

import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import me.noci.advancedtooltip.core.config.AdvancedTooltipConfiguration;
import me.noci.advancedtooltip.core.config.SaturationType;
import me.noci.advancedtooltip.core.utils.FoodInfo;
import me.noci.advancedtooltip.core.utils.IconData;
import net.labymod.api.client.world.item.ItemStack;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.List;

public class ItemFoodData {

    public static List<IconComponent> getIconFoodData(net.minecraft.world.item.ItemStack itemStack) {
        if (!itemStack.isEdible()) return List.of();

        AdvancedTooltipAddon addon = AdvancedTooltipAddon.getInstance();
        AdvancedTooltipConfiguration configuration = addon.configuration();
        SaturationType saturationType = configuration.saturationLevel().get();
        boolean showFoodLevel = configuration.foodLevel().get();

        if (!showFoodLevel && saturationType == SaturationType.HIDDEN) {
            return List.of();
        }

        ItemStack labyItemStack = ItemCast.toLabyItemStack(itemStack);
        FoodInfo foodInfo = addon.getFoodInfo();
        ArrayList<IconComponent> iconComponents = Lists.newArrayList();

        if (saturationType != SaturationType.HIDDEN) {
            List<IconData> icons = Lists.newArrayList();
            float saturationIncrement = saturationType == SaturationType.MAX_SATURATION ? foodInfo.getSaturationIncrement(labyItemStack) : foodInfo.getAddedSaturation(labyItemStack);

            while (saturationIncrement >= 2) {
                saturationIncrement -= 2;
                icons.add(IconData.FULL_SATURATION);
            }

            if (saturationIncrement > 0) {
                icons.add(IconData.HALF_SATURATION);
            }

            iconComponents.add(new IconComponent(icons));
        }

        if (showFoodLevel) {
            List<IconData> icons = Lists.newArrayList();
            int foodLevel = foodInfo.getFoodLevel(labyItemStack);

            while (foodLevel >= 2) {
                foodLevel -= 2;
                icons.add(IconData.FULL_FOOD);
            }

            if (foodLevel > 0) {
                icons.add(IconData.HALF_FOOD);
            }

            iconComponents.add(new IconComponent(icons));
        }

        return iconComponents;
    }


}
