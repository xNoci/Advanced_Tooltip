package me.noci.advancedtooltip.core.utils;

import net.labymod.api.client.world.item.ItemStack;

public class DefaultFoodInfo implements FoodInfo {

    @Override
    public int getFoodLevel(ItemStack itemStack) {
        return itemStack.isFood() ? 0 : -1;
    }

    @Override
    public float getSaturationModifier(ItemStack itemStack) {
        return itemStack.isFood() ? 0 : -1;
    }

}
