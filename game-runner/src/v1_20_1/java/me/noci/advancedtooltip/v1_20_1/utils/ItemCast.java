package me.noci.advancedtooltip.v1_20_1.utils;

import net.labymod.api.client.world.item.ItemStack;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public class ItemCast {

    public static ItemStack toLabyItemStack(net.minecraft.world.item.ItemStack itemStack) {
        return cast(itemStack);
    }

    public static net.minecraft.world.item.ItemStack toMinecraftItemStack(ItemStack itemStack) {
        return cast(itemStack);
    }

    public static Item toMinecraftItem(ItemStack itemStack) {
        return toMinecraftItemStack(itemStack).getItem();
    }

    public static <T extends Item> @Nullable T asItem(ItemStack itemStack, Class<T> clazz) {
        Item item = toMinecraftItem(itemStack);
        if (!clazz.isInstance(item)) return null;
        return cast(item);
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object object) {
        return (T) object;
    }

}
