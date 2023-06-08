package me.noci.advancedtooltip.v1_12_2;

import me.noci.advancedtooltip.core.utils.ItemQuery;
import me.noci.advancedtooltip.v1_12_2.utils.ItemCast;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemRecord;
import org.jetbrains.annotations.Nullable;

import javax.inject.Singleton;

@Singleton
@Implements(ItemQuery.class)
public class VersionedItemQuery implements ItemQuery {

    private static final int ITEM_RECORD_13_ID = Item.getIdFromItem(Items.RECORD_13);

    @Override
    public int getFoodLevel(ItemStack itemStack) {
        FoodProperties foodProperties = getFoodProperties(itemStack);
        return foodProperties != null ? foodProperties.nutrition() : INVALID_ITEM;
    }

    @Override
    public float getSaturationModifier(ItemStack itemStack) {
        FoodProperties foodProperties = getFoodProperties(itemStack);
        return foodProperties != null ? foodProperties.saturationModifier() : INVALID_ITEM;
    }

    @Override
    public int getDiscSignalStrengt(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItemStack(itemStack).getItem();
        if (!(item instanceof ItemRecord)) return INVALID_ITEM;
        return Item.getIdFromItem(item) + 1 - ITEM_RECORD_13_ID;
    }

    @Nullable
    private FoodProperties getFoodProperties(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItemStack(itemStack).getItem();
        if(!(item instanceof ItemFood itemFood)) return null;
        return new FoodProperties(itemFood.getHealAmount(null), itemFood.getSaturationModifier(null));
    }

    private record FoodProperties(int nutrition, float saturationModifier) {}

}
