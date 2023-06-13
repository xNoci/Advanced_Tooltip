package me.noci.advancedtooltip.v1_18_2;

import me.noci.advancedtooltip.core.utils.ItemQuery;
import me.noci.advancedtooltip.v1_18_2.utils.ItemCast;
import net.labymod.api.client.world.effect.PotionEffect;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.labymod.api.nbt.NBTTagType;
import net.labymod.api.nbt.tags.NBTTagCompound;
import net.labymod.api.nbt.tags.NBTTagList;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.SuspiciousStewItem;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Implements(ItemQuery.class)
public class VersionedItemQuery implements ItemQuery {

    @Override
    public boolean isMapItem(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItemStack(itemStack).getItem();
        return item instanceof MapItem;
    }

    @Override
    public int getDiscSignalStrengt(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItemStack(itemStack).getItem();
        if (!(item instanceof RecordItem recordItem)) return INVALID_ITEM;
        return recordItem.getAnalogOutput();
    }

    @Override
    public List<PotionEffect> getStewEffect(ItemStack itemStack) {
        if (!itemStack.hasNBTTag() || !itemStack.getNBTTag().contains("Effects")) return List.of();
        Item item = ItemCast.toMinecraftItemStack(itemStack).getItem();
        if (!(item instanceof SuspiciousStewItem)) return List.of();
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

        return stewEffects;
    }

    @Override
    public @Nullable FoodProperties getFoodProperties(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItemStack(itemStack).getItem();
        var foodProperties = item.getFoodProperties();
        if (foodProperties == null) return null;
        return new FoodProperties(foodProperties.getNutrition(), foodProperties.getSaturationModifier());
    }

    @Override
    public @Nullable String getItemNBTData(ItemStack itemStack, boolean withArrayContent) {
        if (!itemStack.hasNBTTag()) return null;
        return NbtUtils.prettyPrint((Tag) itemStack.getNBTTag(), withArrayContent);
    }

}
