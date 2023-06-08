package me.noci.advancedtooltip.v1_12_2.utils;

import net.labymod.api.client.world.item.ItemStack;

public class ItemCast {

    public static ItemStack toLabyItemStack(net.minecraft.item.ItemStack itemStack) {
        return cast(itemStack, ItemStack.class);
    }

    public static net.minecraft.item.ItemStack toMinecraftItemStack(ItemStack itemStack) {
        return cast(itemStack, net.minecraft.item.ItemStack.class);
    }

    private static <T> T cast(Object object, Class<T> type) {
        return (T) object;
    }

}
