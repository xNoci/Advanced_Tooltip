package me.noci.advancedtooltip.v1_12_2;

import me.noci.advancedtooltip.core.referenceable.ItemQuery;
import me.noci.advancedtooltip.v1_12_2.utils.ItemCast;
import me.noci.advancedtooltip.v1_12_2.utils.NBTPrinter;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemRecord;
import net.minecraft.nbt.NBTBase;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
@Implements(ItemQuery.class)
public class VersionedItemQuery implements ItemQuery {

    private static final int ITEM_RECORD_13_ID = Item.getIdFromItem(Items.RECORD_13);

    @Override
    public boolean isMapItem(ItemStack itemStack) {
        return ItemCast.toMinecraftItem(itemStack).isMap();
    }

    @Override
    public Optional<Integer> getDiscSignalStrengt(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItem(itemStack);
        if (!(item instanceof ItemRecord)) return Optional.empty();
        return Optional.of(Item.getIdFromItem(item) + 1 - ITEM_RECORD_13_ID);
    }

    @Override
    public Optional<FoodProperties> getFoodProperties(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItem(itemStack);
        if (!(item instanceof ItemFood itemFood)) return Optional.empty();
        return Optional.of(new FoodProperties(itemFood.getHealAmount(null), itemFood.getSaturationModifier(null)));
    }

    @Override
    public Optional<String> displayItemData(ItemStack itemStack, boolean withArrayContent, boolean expandComponents) {
        if (!itemStack.hasNBTTag()) return Optional.empty();
        return Optional.of(NBTPrinter.prettyPrint((NBTBase) itemStack.getNBTTag(), withArrayContent));
    }

    @Override
    public boolean isCommandBlock(ItemStack itemStack) {
        return ItemCast.toMinecraftBlockItem(itemStack)
                .filter(blockItem -> blockItem.getBlock() == Blocks.COMMAND_BLOCK)
                .isPresent();
    }

}
