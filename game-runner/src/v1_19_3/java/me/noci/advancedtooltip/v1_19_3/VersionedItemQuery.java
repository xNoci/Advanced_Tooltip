package me.noci.advancedtooltip.v1_19_3;

import me.noci.advancedtooltip.core.utils.ItemQuery;
import me.noci.advancedtooltip.v1_19_3.utils.ItemCast;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.RecordItem;
import org.jetbrains.annotations.Nullable;

import javax.inject.Singleton;

@Singleton
@Implements(ItemQuery.class)
public class VersionedItemQuery implements ItemQuery {

    @Override
    public int getFoodLevel(ItemStack itemStack) {
        FoodProperties foodProperties = getFoodProperties(itemStack);
        return foodProperties != null ? foodProperties.getNutrition() : INVALID_ITEM;
    }

    @Override
    public float getSaturationModifier(ItemStack itemStack) {
        FoodProperties foodProperties = getFoodProperties(itemStack);
        return foodProperties != null ? foodProperties.getSaturationModifier() : INVALID_ITEM;
    }

    @Override
    public int getDiscSignalStrengt(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItemStack(itemStack).getItem();
        if (!(item instanceof RecordItem recordItem)) return INVALID_ITEM;
        return recordItem.getAnalogOutput();
    }

    @Nullable
    private FoodProperties getFoodProperties(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItemStack(itemStack).getItem();
        return item.getFoodProperties();
    }

}
