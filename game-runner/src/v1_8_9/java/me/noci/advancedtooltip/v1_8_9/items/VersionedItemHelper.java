package me.noci.advancedtooltip.v1_8_9.items;

import me.noci.advancedtooltip.core.referenceable.items.ItemHelper;
import me.noci.advancedtooltip.v1_8_9.utils.ItemCast;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemMapBase;
import net.minecraft.item.ItemRecord;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
@Implements(ItemHelper.class)
public class VersionedItemHelper implements ItemHelper {

    private static final int ITEM_RECORD_13_ID = Item.getIdFromItem(Items.record_13);

    @Override
    public boolean isArmor(ItemStack itemStack) {
        return ItemCast.toMinecraftItem(itemStack) instanceof ItemArmor;
    }

    @Override
    public Optional<Integer> armorBars(ItemStack itemStack) {
        return ItemCast.asItem(itemStack, ItemArmor.class).map(itemArmor -> itemArmor.getArmorMaterial().getDamageReductionAmount(itemArmor.armorType));
    }

    @Override
    public Optional<Integer> discSignalStrengt(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItem(itemStack);
        if (!(item instanceof ItemRecord)) return Optional.empty();
        return Optional.of(Item.getIdFromItem(item) + 1 - ITEM_RECORD_13_ID);
    }
}
