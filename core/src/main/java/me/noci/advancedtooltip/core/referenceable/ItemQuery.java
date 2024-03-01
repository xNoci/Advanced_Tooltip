package me.noci.advancedtooltip.core.referenceable;

import me.noci.advancedtooltip.core.utils.MapDecorationLocation;
import me.noci.advancedtooltip.core.utils.SignText;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.world.effect.PotionEffect;
import net.labymod.api.client.world.food.FoodData;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.nbt.NBTTag;
import net.labymod.api.nbt.NBTTagType;
import net.labymod.api.nbt.tags.NBTTagCompound;
import net.labymod.api.nbt.tags.NBTTagList;
import net.labymod.api.reference.annotation.Referenceable;
import net.labymod.api.util.collection.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@Nullable
@Referenceable
public interface ItemQuery {

    default boolean isArmor(ItemStack itemStack) {
        return false;
    }

    default Optional<Integer> getArmorBars(ItemStack itemStack) {
        return Optional.empty();
    }

    default boolean isMapItem(ItemStack itemStack) {
        return false;
    }

    default Optional<List<MapDecorationLocation>> getMapDecorationLocations(ItemStack itemStack) {
        if (!isMapItem(itemStack) || !itemStack.hasNBTTag()) return Optional.empty();

        NBTTagCompound tagCompound = itemStack.getNBTTag();
        if (!tagCompound.contains("Decorations", NBTTagType.LIST)) return Optional.empty();
        NBTTagList<Object, NBTTag<Object>> decorations = tagCompound.getList("Decorations", NBTTagType.COMPOUND);
        if (decorations.isEmpty()) return Optional.empty();

        List<MapDecorationLocation> locations = Lists.newArrayList();
        for (NBTTag<?> decorationTag : decorations.tags()) {
            if (decorationTag.type() != NBTTagType.COMPOUND) return Optional.empty();

            NBTTagCompound mapData = (NBTTagCompound) decorationTag;
            byte type = mapData.getByte("type");
            double x = mapData.getDouble("x");
            double z = mapData.getDouble("z");

            locations.add(new MapDecorationLocation(type, x, z));
        }

        return Optional.of(locations);
    }

    Optional<Integer> getDiscSignalStrengt(ItemStack itemStack);

    default Optional<Integer> getRepairCost(ItemStack itemStack) {
        if (!itemStack.hasNBTTag()) return Optional.empty();
        NBTTagCompound tag = itemStack.getNBTTag();
        if (!tag.contains("RepairCost", NBTTagType.INT)) return Optional.empty();
        return Optional.of(tag.getInt("RepairCost"));
    }

    default Optional<Integer> getAnvilUsages(ItemStack itemStack) {
        Optional<Integer> repairCost = getRepairCost(itemStack);
        return repairCost.map(integer -> log2(integer + 1));
    }

    default Optional<List<PotionEffect>> getStewEffect(ItemStack itemStack) {
        return Optional.empty();
    }

    Optional<FoodProperties> getFoodProperties(ItemStack itemStack);

    default Optional<Integer> getNutrition(ItemStack itemStack) {
        Optional<FoodProperties> foodProperties = getFoodProperties(itemStack);
        return foodProperties.map(FoodProperties::nutrition);
    }

    default Optional<Float> getSaturationModifier(ItemStack itemStack) {
        Optional<FoodProperties> foodProperties = getFoodProperties(itemStack);
        return foodProperties.map(FoodProperties::saturationModifier);
    }

    default Optional<Float> getSaturationIncrement(ItemStack itemStack) {
        Optional<Integer> foodLevel = getNutrition(itemStack);
        Optional<Float> saturationModifier = getSaturationModifier(itemStack);

        if (foodLevel.isEmpty() || saturationModifier.isEmpty()) return Optional.empty();
        return Optional.of(foodLevel.get() * saturationModifier.get() * 2f);
    }

    default Optional<Float> getAddedSaturation(ItemStack itemStack) {
        ClientPlayer clientPlayer = Laby.labyAPI().minecraft().getClientPlayer();
        if (clientPlayer == null) return Optional.empty();

        Optional<Integer> foodLevel = getNutrition(itemStack);
        Optional<Float> saturationModifier = getSaturationModifier(itemStack);
        if (foodLevel.isEmpty() || saturationModifier.isEmpty()) return Optional.empty();

        FoodData foodData = clientPlayer.foodData();
        float currentPlayerSaturation = foodData.getSaturationLevel();
        int newFoodLevel = Math.min(foodData.getFoodLevel() + foodLevel.get(), 20);
        float newSaturation = Math.min(foodData.getSaturationLevel() + saturationModifier.get(), newFoodLevel);

        return Optional.of(newSaturation - currentPlayerSaturation);
    }

    default Optional<String> getItemNBTData(ItemStack itemStack, boolean withArrayContent) {
        return Optional.empty();
    }

    default boolean isCommandBlock(ItemStack itemStack) {
        return false;
    }

    default Optional<String> getCommandBlockCommand(ItemStack itemStack) {
        if (!isCommandBlock(itemStack)) return Optional.empty();
        Optional<NBTTagCompound> tagCompound = getBlockEntityTag(itemStack);
        if (tagCompound.isEmpty()) return Optional.empty();
        NBTTagCompound tag = tagCompound.get();
        if (!tag.contains("Command", NBTTagType.STRING)) return Optional.empty();
        return Optional.of(tag.getString("Command"));
    }

    default Optional<SignText> getSignText(ItemStack itemStack) {
        Optional<NBTTagCompound> blockEntityCompound = getBlockEntityTag(itemStack);
        if (blockEntityCompound.isEmpty()) return Optional.empty();
        NBTTagCompound compound = blockEntityCompound.get();

        int protocolVersion = Laby.labyAPI().minecraft().getProtocolVersion();
        if (protocolVersion >= 763) return SignText.parseAboveOrEquals120(compound);
        if (protocolVersion >= 754) return SignText.parseBelow120(compound);
        return SignText.parseBelowOrEquals112(compound);
    }

    default Optional<NBTTagCompound> getBlockEntityTag(ItemStack itemStack) {
        NBTTagCompound itemCompound = itemStack.getNBTTag();
        if (itemCompound == null || !itemCompound.contains("BlockEntityTag")) return Optional.empty();
        return Optional.of(itemCompound.getCompound("BlockEntityTag"));
    }

    //-------- Utilities --------

    //https://stackoverflow.com/a/3305400
    private static int log2(int x) {
        return (int) (Math.log(x) / Math.log(2) + 1e-10);
    }

    record FoodProperties(int nutrition, float saturationModifier) {
    }

}
