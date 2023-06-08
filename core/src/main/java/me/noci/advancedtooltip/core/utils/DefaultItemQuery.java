package me.noci.advancedtooltip.core.utils;

import net.labymod.api.client.world.item.ItemStack;

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
    public int getDiscSignalStrengt(ItemStack itemStack) {
        return INVALID_ITEM;
    }

}
