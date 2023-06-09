package me.noci.advancedtooltip.core.utils;

import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.world.food.FoodData;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.nbt.NBTTag;
import net.labymod.api.nbt.NBTTagType;
import net.labymod.api.nbt.tags.NBTTagCompound;
import net.labymod.api.nbt.tags.NBTTagList;
import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public interface ItemQuery {

    int INVALID_ITEM = -1;

    int getFoodLevel(ItemStack itemStack);

    float getSaturationModifier(ItemStack itemStack);

    int getDiscSignalStrengt(ItemStack itemStack);

    default boolean isMapItem(ItemStack itemStack) {
        return false;
    }

    @Nullable
    default MapLocation getExplorerMapLocation(ItemStack itemStack) {
        if (!isMapItem(itemStack) || !itemStack.hasNBTTag()) return null;

        NBTTagCompound tagCompound = itemStack.getNBTTag();
        if (!tagCompound.contains("Decorations", NBTTagType.LIST)) return null;
        NBTTagList<Object, NBTTag<Object>> decorations = tagCompound.getList("Decorations", NBTTagType.COMPOUND);
        if (decorations.isEmpty()) return null;

        NBTTag<?> decorationTag = decorations.get(0);
        if (decorationTag.type() != NBTTagType.COMPOUND) return null;

        NBTTagCompound mapData = (NBTTagCompound) decorationTag;
        byte type = mapData.getByte("type");
        double x = mapData.getDouble("x");
        double z = mapData.getDouble("z");

        return new MapLocation(type, x, z);
    }

    default int getRepairCost(ItemStack itemStack) {
        if (!itemStack.hasNBTTag()) return INVALID_ITEM;
        NBTTagCompound tag = itemStack.getNBTTag();
        if (!tag.contains("RepairCost", NBTTagType.INT)) return INVALID_ITEM;
        return tag.getInt("RepairCost");
    }

    default int getUsages(ItemStack itemStack) {
        int repairCost = getRepairCost(itemStack);
        if (repairCost == INVALID_ITEM) return INVALID_ITEM;
        return log2(repairCost + 1);
    }

    default float getSaturationIncrement(ItemStack itemStack) {
        int foodLevel = getFoodLevel(itemStack);
        float saturationModifier = getSaturationModifier(itemStack);

        if (foodLevel == INVALID_ITEM || saturationModifier == INVALID_ITEM) return INVALID_ITEM;

        return foodLevel * saturationModifier * 2f;
    }

    default float getAddedSaturation(ItemStack itemStack) {
        ClientPlayer clientPlayer = Laby.labyAPI().minecraft().getClientPlayer();
        if (clientPlayer == null) return INVALID_ITEM;

        int foodLevel = getFoodLevel(itemStack);
        float saturationModifier = getSaturationModifier(itemStack);

        if (foodLevel == INVALID_ITEM || saturationModifier == INVALID_ITEM) return INVALID_ITEM;

        FoodData foodData = clientPlayer.foodData();

        float currentPlayerSaturation = foodData.getSaturationLevel();

        int newFoodLevel = Math.min(foodData.getFoodLevel() + foodLevel, 20);
        float newSaturation = Math.min(foodData.getSaturationLevel() + saturationModifier, newFoodLevel);

        return newSaturation - currentPlayerSaturation;
    }

    //https://stackoverflow.com/a/3305400
    private static int log2(int x) {
        return (int) (Math.log(x) / Math.log(2) + 1e-10);
    }

}
