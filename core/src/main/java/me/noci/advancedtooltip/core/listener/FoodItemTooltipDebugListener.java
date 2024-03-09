package me.noci.advancedtooltip.core.listener;

import me.noci.advancedtooltip.core.TooltipAddon;
import me.noci.advancedtooltip.core.referenceable.items.FoodItems;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.world.food.FoodData;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.world.ItemStackTooltipEvent;

import java.util.List;
import java.util.Optional;

public class FoodItemTooltipDebugListener {

    private final TooltipAddon addon;
    private final FoodItems foodItems;


    public FoodItemTooltipDebugListener(TooltipAddon addon, FoodItems foodItems) {
        this.addon = addon;
        this.foodItems = foodItems;
    }

    @Subscribe
    public void onToolTip(ItemStackTooltipEvent event) {
        ItemStack itemStack = event.itemStack();
        var debugSettings = addon.configuration().developerSettings().debugMode();
        if (!itemStack.isFood() || !debugSettings.enabled()) {
            return;
        }

        Optional<Float> saturationIncrement = foodItems.saturationIncrement(itemStack);
        Optional<Float> addedSaturation = foodItems.addedSaturation(itemStack);
        if (saturationIncrement.isEmpty() || addedSaturation.isEmpty()) return;

        ClientPlayer clientPlayer = Laby.labyAPI().minecraft().getClientPlayer();
        if (clientPlayer == null) return;

        FoodData foodData = clientPlayer.foodData();
        int newFoodLevel = Math.min(foodData.getFoodLevel() + foodData.getFoodLevel(), 20);
        float newSaturation = Math.min(foodData.getSaturationLevel() + saturationIncrement.get(), newFoodLevel);

        TextColor color = debugSettings.textColor();
        List<Component> tooltip = event.getTooltipLines();
        tooltip.add(Component.text("", color));
        tooltip.add(Component.text("Food level: " + foodData.getFoodLevel(), color));
        tooltip.add(Component.text("Saturation Increment: " + saturationIncrement.get(), color));
        tooltip.add(Component.text("Added Saturation: " + addedSaturation.get(), color));
        tooltip.add(Component.text("", color));
        tooltip.add(Component.text("Current Player Saturation: " + foodData.getSaturationLevel(), color));
        tooltip.add(Component.text("New Saturation: " + newSaturation, color));
        tooltip.add(Component.text("", color));
    }

}
