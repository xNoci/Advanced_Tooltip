package me.noci.advancedtooltip.v1_12_2;

import me.noci.advancedtooltip.core.referenceable.ItemQuery;
import me.noci.advancedtooltip.v1_12_2.utils.ItemCast;
import me.noci.advancedtooltip.v1_12_2.utils.NBTPrinter;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemRecord;
import net.minecraft.nbt.NBTBase;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
@Implements(ItemQuery.class)
public class VersionedItemQuery implements ItemQuery {

    private static final int ITEM_RECORD_13_ID = Item.getIdFromItem(Items.RECORD_13);

    @Override
    public boolean isMapItem(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItemStack(itemStack).getItem();
        return item.isMap();
    }

    @Override
    public Optional<Integer> getDiscSignalStrengt(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItemStack(itemStack).getItem();
        if (!(item instanceof ItemRecord)) return Optional.empty();
        return Optional.of(Item.getIdFromItem(item) + 1 - ITEM_RECORD_13_ID);
    }

    @Override
    public Optional<FoodProperties> getFoodProperties(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItemStack(itemStack).getItem();
        if (!(item instanceof ItemFood itemFood)) return Optional.empty();
        return Optional.of(new FoodProperties(itemFood.getHealAmount(null), itemFood.getSaturationModifier(null)));
    }

    @Override
    public Optional<String> getItemNBTData(ItemStack itemStack, boolean withArrayContent) {
        if (!itemStack.hasNBTTag()) return Optional.empty();
        return Optional.of(NBTPrinter.prettyPrint((NBTBase) itemStack.getNBTTag(), withArrayContent));
    }

    @Override
    public boolean isCommandBlock(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItemStack(itemStack).getItem();
        if (!(item instanceof ItemBlock itemBlock)) return false;
        return itemBlock.getBlock() == Blocks.COMMAND_BLOCK;
    }

}
