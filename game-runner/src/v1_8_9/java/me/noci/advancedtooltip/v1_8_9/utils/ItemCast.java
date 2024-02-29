package me.noci.advancedtooltip.v1_8_9.utils;

import net.labymod.api.client.world.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import java.util.Optional;

public class ItemCast {

    public static net.minecraft.item.ItemStack toMinecraftItemStack(ItemStack itemStack) {
        return cast(itemStack);
    }

    public static Optional<ItemBlock> toMinecraftBlockItem(ItemStack itemStack) {
        if (!itemStack.isBlock()) return Optional.empty();
        ItemBlock item = (ItemBlock) toMinecraftItemStack(itemStack).getItem();
        return Optional.of(item);
    }

    public static Item toMinecraftItem(ItemStack itemStack) {
        return toMinecraftItemStack(itemStack).getItem();
    }

    private static <T> T cast(Object object) {
        return (T) object;
    }

}
