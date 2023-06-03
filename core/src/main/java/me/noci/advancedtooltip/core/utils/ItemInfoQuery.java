package me.noci.advancedtooltip.core.utils;

import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.world.ItemStackTooltipEvent;
import net.labymod.api.nbt.NBTTagType;
import net.labymod.api.nbt.tags.NBTTagCompound;

public class ItemInfoQuery {

    public static int getRepairCost(ItemStack itemStack) {
        if(!itemStack.hasNBTTag()) return 0;
        NBTTagCompound tag = itemStack.getNBTTag();
        if(!tag.contains("RepairCost", NBTTagType.INT)) return 0;
        return tag.getInt("RepairCost");
    }

    public static int getUsages(ItemStack itemStack) {
        int repairCost = getRepairCost(itemStack);
        if(repairCost == 0) return 0;
        return log2(repairCost + 1);
    }

    //https://stackoverflow.com/a/3305400
    private static int log2(int x) {
        return (int) (Math.log(x) / Math.log(2) + 1e-10);
    }


}
