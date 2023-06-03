package me.noci.advancedtooltip.v1_19_4.util;

import net.labymod.api.client.world.item.ItemStack;

public class ItemCast {

    public static ItemStack toLabyItemStack(net.minecraft.world.item.ItemStack itemStack) {
        return cast(itemStack, ItemStack.class);
    }

    public static net.minecraft.world.item.ItemStack toMinecraftItemStack(ItemStack itemStack) {
        return cast(itemStack, net.minecraft.world.item.ItemStack.class);
    }

    private static <T> T cast(Object object, Class<T> type) {
        return (T) object;
    }

}
