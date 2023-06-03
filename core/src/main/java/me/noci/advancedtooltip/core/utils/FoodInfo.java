package me.noci.advancedtooltip.core.utils;

import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.world.food.FoodData;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public interface FoodInfo {

    int getFoodLevel(ItemStack itemStack);

    float getSaturationModifier(ItemStack itemStack);

    default float getSaturationIncrement(ItemStack itemStack) {
        return getFoodLevel(itemStack) * getSaturationModifier(itemStack) * 2f;
    }

    default float getAddedSaturation(ItemStack itemStack) {
        ClientPlayer clientPlayer = Laby.labyAPI().minecraft().getClientPlayer();
        if (clientPlayer == null) return 0;
        FoodData foodData = clientPlayer.foodData();

        float currentPlayerSaturation = foodData.getSaturationLevel();

        int newFoodLevel = Math.min(foodData.getFoodLevel() + getFoodLevel(itemStack), 20);
        float newSaturation = Math.min(foodData.getSaturationLevel() + getSaturationIncrement(itemStack), newFoodLevel);

        return newSaturation - currentPlayerSaturation;
    }

}
