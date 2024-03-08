package me.noci.advancedtooltip.v1_12_2.utils;

import net.labymod.api.client.world.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import java.util.Optional;

public class ItemCast {

    public static net.minecraft.item.ItemStack toMinecraftItemStack(ItemStack itemStack) {
        return cast(itemStack);
    }

    public static Item toMinecraftItem(ItemStack itemStack) {
        return toMinecraftItemStack(itemStack).getItem();
    }

    public static <T extends Item> Optional<T> asItem(ItemStack itemStack, Class<T> clazz) {
        Item item = toMinecraftItem(itemStack);
        if (!clazz.isInstance(item)) return Optional.empty();
        return Optional.of(cast(item));
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object object) {
        return (T) object;
    }

}
