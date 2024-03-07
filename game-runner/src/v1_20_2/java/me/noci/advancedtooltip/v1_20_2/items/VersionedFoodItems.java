package me.noci.advancedtooltip.v1_20_2.items;

import me.noci.advancedtooltip.core.referenceable.items.FoodItems;
import me.noci.advancedtooltip.v1_20_2.utils.ItemCast;
import net.labymod.api.client.world.effect.PotionEffect;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
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
        if (!itemStack.hasTag() || !itemStack.getTag().contains("effects")) return Optional.empty();

        ListTag effects = itemStack.getTag().getList("effects", ListTag.TAG_COMPOUND);
        ArrayList<PotionEffect> stewEffects = org.apache.commons.compress.utils.Lists.newArrayList();

        effects.stream().map(tag -> (CompoundTag) tag).forEach(effect -> {
            int duration = SuspiciousStewItem.DEFAULT_DURATION;
            if (effect.contains("duration", CompoundTag.TAG_INT)) {
                duration = effect.getInt("duration");
            }

            MobEffect mobEffect = BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.tryParse(effect.getString("id")));
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
