package me.noci.advancedtooltip.core.referenceable.items;

import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.world.effect.PotionEffect;
import net.labymod.api.client.world.food.FoodData;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Nullable
@Referenceable
public interface FoodItems {

    FoodItems DEFAULT = new DefaultFoodItems();

    @Nullable
    List<PotionEffect> stewEffect(ItemStack itemStack);

    @Nullable
    FoodProperties foodProperties(ItemStack itemStack);

    int nutrition(ItemStack itemStack);

    float saturationIncrement(ItemStack itemStack);

    float addedSaturation(ItemStack itemStack);

    class DefaultFoodItems implements FoodItems {
        @Override
        public @Nullable List<PotionEffect> stewEffect(ItemStack itemStack) {
            return null;
        }

        @Override
        public @Nullable FoodProperties foodProperties(ItemStack itemStack) {
            return null;
        }

        @Override
        public int nutrition(ItemStack itemStack) {
            FoodProperties foodProperties = foodProperties(itemStack);
            if (foodProperties == null) return 0;
            return foodProperties.nutrition();
        }

        @Override
        public float saturationIncrement(ItemStack itemStack) {
            int nutrition = nutrition(itemStack);
            float saturationModifier = saturationModifier(itemStack);

            if (nutrition <= 0 || saturationModifier <= 0) return 0;
            return nutrition * saturationModifier * 2f;
        }

        @Override
        public float addedSaturation(ItemStack itemStack) {
            ClientPlayer clientPlayer = Laby.labyAPI().minecraft().getClientPlayer();
            if (clientPlayer == null) return 0;

            int foodLevel = nutrition(itemStack);
            float saturationModifier = saturationModifier(itemStack);
            if (foodLevel <= 0 || saturationModifier <= 0) return 0;

            FoodData foodData = clientPlayer.foodData();

            int newFoodLevel = Math.min(foodData.getFoodLevel() + foodLevel, 20);
            float saturationLevel = foodData.getSaturationLevel();
            float newSaturation = Math.min(saturationLevel + saturationModifier, newFoodLevel);

            return newSaturation - saturationLevel;
        }

        private float saturationModifier(ItemStack itemStack) {
            FoodProperties foodProperties = foodProperties(itemStack);
            if (foodProperties == null) return 0;
            return foodProperties.saturationModifier();
        }

    }

    record FoodProperties(int nutrition, float saturationModifier) {
    }

}
