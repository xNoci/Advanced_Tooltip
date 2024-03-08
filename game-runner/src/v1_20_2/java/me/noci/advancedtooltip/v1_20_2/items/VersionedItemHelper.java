package me.noci.advancedtooltip.v1_20_2.items;

import me.noci.advancedtooltip.core.referenceable.items.ItemHelper;
import me.noci.advancedtooltip.v1_20_2.utils.ItemCast;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.block.CommandBlock;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
@Implements(ItemHelper.class)
public class VersionedItemHelper implements ItemHelper {

    @Override
    public boolean isArmor(ItemStack itemStack) {
        return ItemCast.toMinecraftItem(itemStack) instanceof ArmorItem;
    }

    @Override
    public Optional<Integer> armorBars(ItemStack itemStack) {
        return ItemCast.asItem(itemStack, ArmorItem.class).map(ArmorItem::getDefense);
    }

    @Override
    public Optional<Integer> discSignalStrengt(ItemStack itemStack) {
        return ItemCast.asItem(itemStack, RecordItem.class).map(RecordItem::getAnalogOutput);
    }

}
