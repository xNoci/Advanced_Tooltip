package me.noci.advancedtooltip.core.listener;

import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import me.noci.advancedtooltip.core.referenceable.ItemQuery;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.world.food.FoodData;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.world.ItemStackTooltipEvent;

import java.util.List;
import java.util.Optional;

public class FoodItemTooltipDebugListener {

    private final AdvancedTooltipAddon addon;
    private final ItemQuery itemQuery;


    public FoodItemTooltipDebugListener(AdvancedTooltipAddon addon, ItemQuery itemQuery) {
        this.addon = addon;
        this.itemQuery = itemQuery;
    }

    @Subscribe
    public void onToolTip(ItemStackTooltipEvent event) {
        ItemStack itemStack = event.itemStack();
        if (!itemStack.isFood() || !addon.configuration().developerSettings().debugMode().get()) {
            return;
        }

        Optional<Float> saturationIncrement = itemQuery.getSaturationIncrement(itemStack);
        Optional<Float> addedSaturation = itemQuery.getAddedSaturation(itemStack);
        if (saturationIncrement.isEmpty() || addedSaturation.isEmpty()) return;

        ClientPlayer clientPlayer = Laby.labyAPI().minecraft().getClientPlayer();
        if (clientPlayer == null) return;

        FoodData foodData = clientPlayer.foodData();
        int newFoodLevel = Math.min(foodData.getFoodLevel() + foodData.getFoodLevel(), 20);
        float newSaturation = Math.min(foodData.getSaturationLevel() + saturationIncrement.get(), newFoodLevel);

        List<Component> tooltip = event.getTooltipLines();
        tooltip.add(Component.text(""));
        tooltip.add(Component.text("Food level: " + foodData.getFoodLevel()));
        tooltip.add(Component.text("Saturation Increment: " + saturationIncrement.get()));
        tooltip.add(Component.text("Added Saturation: " + addedSaturation.get()));
        tooltip.add(Component.text(""));
        tooltip.add(Component.text("Current Player Saturation: " + foodData.getSaturationLevel()));
        tooltip.add(Component.text("New Saturation: " + newSaturation));
        tooltip.add(Component.text(""));
    }

}
