package me.noci.advancedtooltip.v1_12_2.items;

import me.noci.advancedtooltip.core.referenceable.items.FoodItems;
import me.noci.advancedtooltip.v1_12_2.utils.ItemCast;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import org.jetbrains.annotations.Nullable;

import javax.inject.Singleton;

@Singleton
@Implements(FoodItems.class)
public class VersionedFoodItems extends FoodItems.DefaultFoodItems {

    @Override
    public @Nullable FoodProperties foodProperties(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItem(itemStack);
        if (!(item instanceof ItemFood itemFood)) return null;
        return new FoodProperties(itemFood.getHealAmount(null), itemFood.getSaturationModifier(null));
    }
}
