package me.noci.advancedtooltip.v1_18_2;

import me.noci.advancedtooltip.core.utils.ItemQuery;
import me.noci.advancedtooltip.v1_18_2.utils.ItemCast;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.RecordItem;
import org.jetbrains.annotations.Nullable;

import javax.inject.Singleton;

@Singleton
@Implements(ItemQuery.class)
public class VersionedItemQuery implements ItemQuery {

    @Override
    public @Nullable FoodProperties getFoodProperties(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItemStack(itemStack).getItem();
        var foodProperties = item.getFoodProperties();
        if (foodProperties == null) return null;
        return new FoodProperties(foodProperties.getNutrition(), foodProperties.getSaturationModifier());
    }

    @Override
    public int getDiscSignalStrengt(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItemStack(itemStack).getItem();
        if (!(item instanceof RecordItem recordItem)) return INVALID_ITEM;
        return recordItem.getAnalogOutput();
    }

    @Override
    public boolean isMapItem(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItemStack(itemStack).getItem();
        return item instanceof MapItem;
    }

}
