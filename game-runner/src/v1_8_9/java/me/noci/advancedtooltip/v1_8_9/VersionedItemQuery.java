package me.noci.advancedtooltip.v1_8_9;

import me.noci.advancedtooltip.core.utils.ItemQuery;
import me.noci.advancedtooltip.v1_8_9.utils.ItemCast;
import me.noci.advancedtooltip.v1_8_9.utils.NBTPrinter;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemRecord;
import net.minecraft.nbt.NBTBase;
import org.jetbrains.annotations.Nullable;

import javax.inject.Singleton;

@Singleton
@Implements(ItemQuery.class)
public class VersionedItemQuery implements ItemQuery {

    private static final int ITEM_RECORD_13_ID = Item.getIdFromItem(Items.record_13);

    @Override
    public int getDiscSignalStrengt(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItemStack(itemStack).getItem();
        if (!(item instanceof ItemRecord)) return INVALID_ITEM;
        return Item.getIdFromItem(item) + 1 - ITEM_RECORD_13_ID;
    }

    @Override
    public @Nullable FoodProperties getFoodProperties(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItemStack(itemStack).getItem();
        if (!(item instanceof ItemFood itemFood)) return null;
        return new ItemQuery.FoodProperties(itemFood.getHealAmount(null), itemFood.getSaturationModifier(null));
    }

    @Override
    public @Nullable String getItemNBTData(ItemStack itemStack, boolean withArrayContent) {
        if (!itemStack.hasNBTTag()) return null;
        return NBTPrinter.prettyPrint((NBTBase) itemStack.getNBTTag(), withArrayContent);
    }

}
