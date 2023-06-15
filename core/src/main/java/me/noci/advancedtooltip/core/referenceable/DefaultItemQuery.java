package me.noci.advancedtooltip.core.referenceable;

import net.labymod.api.client.world.item.ItemStack;

import java.util.Optional;

public class DefaultItemQuery implements ItemQuery {

    @Override
    public Optional<Integer> getDiscSignalStrengt(ItemStack itemStack) {
        return Optional.empty();
    }

    @Override
    public Optional<FoodProperties> getFoodProperties(ItemStack itemStack) {
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getNutrition(ItemStack itemStack) {
        return Optional.empty();
    }

    @Override
    public Optional<Float> getSaturationModifier(ItemStack itemStack) {
        return Optional.empty();
    }

}
