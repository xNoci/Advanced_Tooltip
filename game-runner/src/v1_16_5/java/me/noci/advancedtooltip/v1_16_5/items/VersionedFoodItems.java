package me.noci.advancedtooltip.v1_16_5.items;

import me.noci.advancedtooltip.core.referenceable.items.FoodItems;
import me.noci.advancedtooltip.v1_16_5.utils.ItemCast;
import net.labymod.api.client.world.effect.PotionEffect;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.labymod.api.util.collection.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SuspiciousStewItem;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
@Implements(FoodItems.class)
public class VersionedFoodItems extends FoodItems.DefaultFoodItems {

    @Override
    public Optional<List<PotionEffect>> stewEffect(ItemStack labyItemStack) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);

        if (!(itemStack.getItem() instanceof SuspiciousStewItem)) return Optional.empty();
        if (!itemStack.hasTag() || !itemStack.getTag().contains("Effects")) return Optional.empty();

        ListTag effects = itemStack.getTag().getList("Effects", 10 /*NBTTagCompound*/);
        ArrayList<PotionEffect> stewEffects = Lists.newArrayList();

        effects.stream().map(tag -> (CompoundTag) tag).forEach(effect -> {
            int duration = 160;
            if (effect.contains("EffectDuration", 3 /*int*/)) {
                duration = effect.getInt("EffectDuration");
            }

            MobEffect mobEffect = MobEffect.byId(effect.getInt("EffectId"));
            stewEffects.add((PotionEffect) new MobEffectInstance(mobEffect, duration));
        });

        return Optional.of(stewEffects);
    }

    @Override
    public Optional<FoodProperties> foodProperties(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItem(itemStack);
        var foodProperties = item.getFoodProperties();
        if (foodProperties == null) return Optional.empty();
        return Optional.of(new FoodProperties(foodProperties.getNutrition(), foodProperties.getSaturationModifier()));
    }
}
