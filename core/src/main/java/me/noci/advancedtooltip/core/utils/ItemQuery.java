package me.noci.advancedtooltip.core.utils;

import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.world.food.FoodData;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.nbt.NBTTagType;
import net.labymod.api.nbt.tags.NBTTagCompound;
import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public interface ItemQuery {

    int getFoodLevel(ItemStack itemStack);

    float getSaturationModifier(ItemStack itemStack);

    default int getRepairCost(ItemStack itemStack) {
        if (!itemStack.hasNBTTag()) return 0;
        NBTTagCompound tag = itemStack.getNBTTag();
        if (!tag.contains("RepairCost", NBTTagType.INT)) return 0;
        return tag.getInt("RepairCost");
    }

    default int getUsages(ItemStack itemStack) {
        int repairCost = getRepairCost(itemStack);
        if (repairCost == 0) return 0;
        return log2(repairCost + 1);
    }

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

    //https://stackoverflow.com/a/3305400
    private static int log2(int x) {
        return (int) (Math.log(x) / Math.log(2) + 1e-10);
    }

}
