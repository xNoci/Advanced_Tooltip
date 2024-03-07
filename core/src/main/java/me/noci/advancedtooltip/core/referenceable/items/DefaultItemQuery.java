package me.noci.advancedtooltip.core.referenceable.items;

import net.labymod.api.client.world.item.ItemStack;

import java.util.Optional;

public class DefaultItemQuery implements ItemQuery {

    @Override
    public Optional<Integer> getDiscSignalStrengt(ItemStack itemStack) {
        return Optional.empty();
    }


}
