package me.noci.advancedtooltip.v1_8_9.utils;

import net.labymod.api.client.world.item.ItemStack;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;

public class ItemCast {

    public static net.minecraft.item.ItemStack toMinecraftItemStack(ItemStack itemStack) {
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
