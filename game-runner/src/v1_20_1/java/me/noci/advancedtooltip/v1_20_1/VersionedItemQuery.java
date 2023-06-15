package me.noci.advancedtooltip.v1_20_1;

import me.noci.advancedtooltip.core.utils.ItemQuery;
import me.noci.advancedtooltip.v1_20_1.util.ItemCast;
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

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
@Implements(ItemQuery.class)
public class VersionedItemQuery implements ItemQuery {

    @Override
    public boolean isMapItem(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItemStack(itemStack).getItem();
        return item instanceof MapItem;
    }

    @Override
    public Optional<Integer> getDiscSignalStrengt(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItemStack(itemStack).getItem();
        if (!(item instanceof RecordItem recordItem)) return Optional.empty();
        return Optional.of(recordItem.getAnalogOutput());
    }

    @Override
    public Optional<List<PotionEffect>> getStewEffect(ItemStack itemStack) {
        if (!itemStack.hasNBTTag() || !itemStack.getNBTTag().contains("Effects")) return Optional.empty();
        Item item = ItemCast.toMinecraftItemStack(itemStack).getItem();
        if (!(item instanceof SuspiciousStewItem)) return Optional.empty();
        NBTTagList effects = itemStack.getNBTTag().getList("Effects", NBTTagType.COMPOUND);

        ArrayList<PotionEffect> stewEffects = Lists.newArrayList();
        for (int i = 0; i < effects.size(); i++) {
            NBTTagCompound effect = (NBTTagCompound) effects.get(i);

            int duration = SuspiciousStewItem.DEFAULT_DURATION;
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
        Item item = ItemCast.toMinecraftItemStack(itemStack).getItem();
        var foodProperties = item.getFoodProperties();
        if (foodProperties == null) return Optional.empty();
        return Optional.of(new FoodProperties(foodProperties.getNutrition(), foodProperties.getSaturationModifier()));
    }

    @Override
    public Optional<String> getItemNBTData(ItemStack itemStack, boolean withArrayContent) {
        if (!itemStack.hasNBTTag()) return Optional.empty();
        return Optional.of(NbtUtils.prettyPrint((Tag) itemStack.getNBTTag(), withArrayContent));
    }

}
