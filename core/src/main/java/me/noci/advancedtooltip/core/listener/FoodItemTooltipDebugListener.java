package me.noci.advancedtooltip.core.listener;

import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import me.noci.advancedtooltip.core.utils.FoodInfo;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.world.food.FoodData;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.world.ItemStackTooltipEvent;

public class FoodItemTooltipDebugListener {

    private final AdvancedTooltipAddon addon;
    private final FoodInfo foodInfo;


    public FoodItemTooltipDebugListener(AdvancedTooltipAddon addon, FoodInfo foodInfo) {
        this.addon = addon;
        this.foodInfo = foodInfo;
    }

    @Subscribe
    public void onToolTip(ItemStackTooltipEvent event) {
        ItemStack itemStack = event.itemStack();
        if (!itemStack.isFood() || !addon.configuration().debugMode().get()) {
            return;
        }

        ClientPlayer clientPlayer = Laby.labyAPI().minecraft().getClientPlayer();
        if(clientPlayer == null) return;
        FoodData foodData = clientPlayer.foodData();

        int newFoodLevel = Math.min(foodData.getFoodLevel() + foodData.getFoodLevel(), 20);
        float newSaturation = Math.min(foodData.getSaturationLevel() + foodInfo.getSaturationIncrement(itemStack), newFoodLevel);

        event.getTooltipLines().add(Component.text(""));
        event.getTooltipLines().add(Component.text("Food level: " + foodData.getFoodLevel()));
        event.getTooltipLines().add(Component.text("Saturation Increment: " + foodInfo.getSaturationIncrement(itemStack)));
        event.getTooltipLines().add(Component.text("Added Saturation: " + foodInfo.getAddedSaturation(itemStack)));
        event.getTooltipLines().add(Component.text(""));
        event.getTooltipLines().add(Component.text("Current Player Saturation: " + foodData.getSaturationLevel()));
        event.getTooltipLines().add(Component.text("New Saturation: " + newSaturation));
        event.getTooltipLines().add(Component.text(""));
    }

}
