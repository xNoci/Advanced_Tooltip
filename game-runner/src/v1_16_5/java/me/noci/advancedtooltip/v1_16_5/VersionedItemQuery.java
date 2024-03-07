package me.noci.advancedtooltip.v1_16_5;

import me.noci.advancedtooltip.core.referenceable.items.ItemQuery;
import me.noci.advancedtooltip.v1_16_5.utils.ItemCast;
import me.noci.advancedtooltip.v1_16_5.utils.NBTPrinter;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.CommandBlock;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
@Implements(ItemQuery.class)
public class VersionedItemQuery implements ItemQuery {

    @Override
    public boolean isMapItem(ItemStack itemStack) {
        return ItemCast.toMinecraftItem(itemStack) instanceof MapItem;
    }

    @Override
    public Optional<Integer> getDiscSignalStrengt(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItem(itemStack);
        if (!(item instanceof RecordItem recordItem)) return Optional.empty();
        return Optional.of(recordItem.getAnalogOutput());
    }

    @Override
    public Optional<String> displayItemData(ItemStack itemStack, boolean withArrayContent) {
        if (!itemStack.hasNBTTag()) return Optional.empty();
        return Optional.of(NBTPrinter.prettyPrint((Tag) itemStack.getNBTTag(), withArrayContent));
    }

    @Override
    public boolean isCommandBlock(ItemStack itemStack) {
        return ItemCast.toMinecraftBlockItem(itemStack)
                .filter(blockItem -> blockItem.getBlock() instanceof CommandBlock)
                .isPresent();
    }

}
