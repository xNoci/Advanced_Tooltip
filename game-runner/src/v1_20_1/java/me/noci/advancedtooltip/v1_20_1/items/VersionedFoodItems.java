package me.noci.advancedtooltip.v1_20_1.items;

import me.noci.advancedtooltip.core.referenceable.items.FoodItems;
import me.noci.advancedtooltip.v1_20_1.utils.ItemCast;
import net.labymod.api.client.world.effect.PotionEffect;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SuspiciousStewItem;

import javax.inject.Singleton;
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

        ListTag effects = itemStack.getTag().getList("Effects", ListTag.TAG_COMPOUND);
        List<PotionEffect> stewEffects = effects.stream().map(tag -> {
            var compound = (CompoundTag) tag;
            int duration = SuspiciousStewItem.DEFAULT_DURATION;
            if (compound.contains("EffectDuration", CompoundTag.TAG_INT)) {
                duration = compound.getInt("EffectDuration");
            }

            MobEffect mobEffect = MobEffect.byId(compound.getInt("EffectId"));
            return (PotionEffect) new MobEffectInstance(mobEffect, duration);
        }).toList();

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
