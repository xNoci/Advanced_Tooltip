package me.noci.advancedtooltip.v1_17_1;

import me.noci.advancedtooltip.core.referenceable.ItemQuery;
import me.noci.advancedtooltip.v1_17_1.utils.ItemCast;
import net.labymod.api.client.world.effect.PotionEffect;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.labymod.api.nbt.NBTTagType;
import net.labymod.api.nbt.tags.NBTTagCompound;
import net.labymod.api.nbt.tags.NBTTagList;
import net.labymod.api.util.collection.Lists;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.CommandBlock;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
@Implements(ItemQuery.class)
public class VersionedItemQuery implements ItemQuery {

    @Override
    public boolean isMapItem(ItemStack itemStack) {
        return ItemCast.toMinecraftItem(itemStack) instanceof MapItem;
    }

    @Override
    public Optional<Integer> getDiscSignalStrengt(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItem(itemStack);
        if (!(item instanceof RecordItem recordItem)) return Optional.empty();
        return Optional.of(recordItem.getAnalogOutput());
    }

    @Override
    public Optional<List<PotionEffect>> getStewEffect(ItemStack itemStack) {
        if (!itemStack.hasNBTTag() || !itemStack.getNBTTag().contains("Effects")) return Optional.empty();
        Item item = ItemCast.toMinecraftItem(itemStack);
        if (!(item instanceof SuspiciousStewItem)) return Optional.empty();
        NBTTagList effects = itemStack.getNBTTag().getList("Effects", NBTTagType.COMPOUND);

        ArrayList<PotionEffect> stewEffects = Lists.newArrayList();
        for (int i = 0; i < effects.size(); i++) {
            NBTTagCompound effect = (NBTTagCompound) effects.get(i);

            int duration = 160;
            if (effect.contains("EffectDuration", NBTTagType.INT)) {
                duration = effect.getInt("EffectDuration");
            }

            MobEffect mobEffect = MobEffect.byId(effect.getInt("EffectId"));
            stewEffects.add((PotionEffect) new MobEffectInstance(mobEffect, duration));
        }

        return Optional.of(stewEffects);
    }

    @Override
    public Optional<FoodProperties> getFoodProperties(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItem(itemStack);
        var foodProperties = item.getFoodProperties();
        if (foodProperties == null) return Optional.empty();
        return Optional.of(new FoodProperties(foodProperties.getNutrition(), foodProperties.getSaturationModifier()));
    }

    @Override
    public Optional<String> displayItemData(ItemStack itemStack, boolean withArrayContent, boolean expandComponents) {
        if (!itemStack.hasNBTTag()) return Optional.empty();
        return Optional.of(NbtUtils.prettyPrint((Tag) itemStack.getNBTTag(), withArrayContent));
    }

    @Override
    public boolean isCommandBlock(ItemStack itemStack) {
        return ItemCast.toMinecraftBlockItem(itemStack)
                .filter(blockItem -> blockItem.getBlock() instanceof CommandBlock)
                .isPresent();
    }

}
