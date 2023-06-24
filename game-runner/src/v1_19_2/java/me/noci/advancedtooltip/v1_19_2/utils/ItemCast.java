package me.noci.advancedtooltip.v1_19_2.utils;

import net.labymod.api.client.world.item.ItemStack;
import net.minecraft.world.item.BlockItem;

import java.util.Optional;

public class ItemCast {

    public static ItemStack toLabyItemStack(net.minecraft.world.item.ItemStack itemStack) {
        return cast(itemStack);
    }

    public static net.minecraft.world.item.ItemStack toMinecraftItemStack(ItemStack itemStack) {
        return cast(itemStack);
    }

    public static Optional<BlockItem> toMinecraftBlockItem(ItemStack itemStack) {
        if (!itemStack.isBlock()) return Optional.empty();
        BlockItem item = (BlockItem) toMinecraftItemStack(itemStack).getItem();
        return Optional.of(item);
    }

    private static <T> T cast(Object object) {
        return (T) object;
    }

}
