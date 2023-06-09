package me.noci.advancedtooltip.core.utils;

import net.labymod.api.client.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class DefaultItemQuery implements ItemQuery {

    @Override
    public int getFoodLevel(ItemStack itemStack) {
        return itemStack.isFood() ? 0 : INVALID_ITEM;
    }

    @Override
    public float getSaturationModifier(ItemStack itemStack) {
        return itemStack.isFood() ? 0 : INVALID_ITEM;
    }

    @Override
    public @Nullable FoodProperties getFoodProperties(ItemStack itemStack) {
        return null;
    }

    @Override
    public int getDiscSignalStrengt(ItemStack itemStack) {
        return INVALID_ITEM;
    }

}
