package me.noci.advancedtooltip.core.referenceable.items;

import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.world.effect.PotionEffect;
import net.labymod.api.client.world.food.FoodData;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@Nullable
@Referenceable
public interface FoodItems {

    FoodItems DEFAULT = new DefaultFoodItems();

    Optional<List<PotionEffect>> stewEffect(ItemStack itemStack);

    Optional<FoodProperties> foodProperties(ItemStack itemStack);

    Optional<Integer> nutrition(ItemStack itemStack);

    Optional<Float> saturationIncrement(ItemStack itemStack);

    Optional<Float> addedSaturation(ItemStack itemStack);

    class DefaultFoodItems implements FoodItems {
        @Override
        public Optional<List<PotionEffect>> stewEffect(ItemStack itemStack) {
            return Optional.empty();
        }

        @Override
        public Optional<FoodProperties> foodProperties(ItemStack itemStack) {
            return Optional.empty();
        }

        @Override
        public Optional<Integer> nutrition(ItemStack itemStack) {
            Optional<FoodProperties> foodProperties = foodProperties(itemStack);
            return foodProperties.map(FoodProperties::nutrition);
        }

        @Override
        public Optional<Float> saturationIncrement(ItemStack itemStack) {
            Optional<Integer> nutrition = nutrition(itemStack);
            Optional<Float> saturationModifier = saturationModifier(itemStack);

            if (nutrition.isEmpty() || saturationModifier.isEmpty()) return Optional.empty();
            return Optional.of(nutrition.get() * saturationModifier.get() * 2f);
        }

        @Override
        public Optional<Float> addedSaturation(ItemStack itemStack) {
            ClientPlayer clientPlayer = Laby.labyAPI().minecraft().getClientPlayer();
            if (clientPlayer == null) return Optional.empty();

            Optional<Integer> foodLevel = nutrition(itemStack);
            Optional<Float> saturationModifier = saturationModifier(itemStack);
            if (foodLevel.isEmpty() || saturationModifier.isEmpty()) return Optional.empty();

            FoodData foodData = clientPlayer.foodData();

            int newFoodLevel = Math.min(foodData.getFoodLevel() + foodLevel.get(), 20);
            float saturationLevel = foodData.getSaturationLevel();
            float newSaturation = Math.min(saturationLevel + saturationModifier.get(), newFoodLevel);

            return Optional.of(newSaturation - saturationLevel);
        }

        private Optional<Float> saturationModifier(ItemStack itemStack) {
            Optional<FoodProperties> foodProperties = foodProperties(itemStack);
            return foodProperties.map(FoodProperties::saturationModifier);
        }

    }

    record FoodProperties(int nutrition, float saturationModifier) {
    }

}
