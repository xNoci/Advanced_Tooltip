package me.noci.advancedtooltip.core.referenceable.items;

import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@Nullable
@Referenceable
public interface ItemHelper {

    ItemHelper DEFAULT = new ItemHelper() {
        @Override
        public boolean isArmor(ItemStack itemStack) {
            return false;
        }

        @Override
        public Optional<Integer> armorBars(ItemStack itemStack) {
            return Optional.empty();
        }

        @Override
        public Optional<Integer> discSignalStrengt(ItemStack itemStack) {
            return Optional.empty();
        }
    };

    boolean isArmor(ItemStack itemStack);

    Optional<Integer> armorBars(ItemStack itemStack);

    Optional<Integer> discSignalStrengt(ItemStack itemStack);

}
