package me.noci.advancedtooltip.v1_18_2.items;

import me.noci.advancedtooltip.core.referenceable.items.FoodItems;
import me.noci.advancedtooltip.v1_18_2.utils.ItemCast;
import net.labymod.api.client.world.effect.PotionEffect;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SuspiciousStewItem;
import org.jetbrains.annotations.Nullable;

import javax.inject.Singleton;
import java.util.List;

@Singleton
@Implements(FoodItems.class)
public class VersionedFoodItems extends FoodItems.DefaultFoodItems {

    @Override
    public @Nullable List<PotionEffect> stewEffect(ItemStack labyItemStack) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);

        if (!(itemStack.getItem() instanceof SuspiciousStewItem)) return null;
        if (!itemStack.hasTag() || !itemStack.getTag().contains("Effects")) return null;

        ListTag effects = itemStack.getTag().getList("Effects", ListTag.TAG_COMPOUND);
        return effects.stream().map(tag -> {
            var compound = (CompoundTag) tag;
            int duration = 160;
            if (compound.contains("EffectDuration", CompoundTag.TAG_INT)) {
                duration = compound.getInt("EffectDuration");
            }

            MobEffect mobEffect = MobEffect.byId(compound.getInt("EffectId"));
            return (PotionEffect) new MobEffectInstance(mobEffect, duration);
        }).toList();
    }

    @Override
    public @Nullable FoodProperties foodProperties(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItem(itemStack);
        var foodProperties = item.getFoodProperties();
        if (foodProperties == null) return null;
        return new FoodProperties(foodProperties.getNutrition(), foodProperties.getSaturationModifier());
    }

}
