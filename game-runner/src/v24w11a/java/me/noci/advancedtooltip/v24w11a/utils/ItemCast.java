package me.noci.advancedtooltip.v24w11a.utils;

import net.labymod.api.client.world.item.ItemStack;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.Item;

import java.util.Optional;

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

    public static <T extends Item> Optional<T> asItem(ItemStack itemStack, Class<T> clazz) {
        Item item = toMinecraftItem(itemStack);
        if (!clazz.isInstance(item)) return Optional.empty();
        return Optional.of(cast(item));
    }

    public static <T> Optional<T> typedDataComponent(ItemStack itemStack, DataComponentType<T> component) {
        var item = ItemCast.toMinecraftItemStack(itemStack);
        return Optional.ofNullable(item.get(component));
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object object) {
        return (T) object;
    }

}
