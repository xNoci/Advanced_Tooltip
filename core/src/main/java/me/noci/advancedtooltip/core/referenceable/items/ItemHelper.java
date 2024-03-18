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
        public boolean isMiningTool(ItemStack itemStack) {
            return false;
        }

        @Override
        public boolean isClock(ItemStack itemStack) {
            return false;
        }

        @Override
        public Optional<Integer> armorBars(ItemStack itemStack) {
            return Optional.empty();
        }

        @Override
        public Optional<Integer> miningLevel(ItemStack itemStack) {
            return Optional.empty();
        }

        @Override
        public Optional<Float> miningSpeed(ItemStack itemStack, boolean applyEnchantments) {
            return Optional.empty();
        }

        @Override
        public Optional<Integer> discSignalStrengt(ItemStack itemStack) {
            return Optional.empty();
        }
    };

    boolean isArmor(ItemStack itemStack);

    boolean isMiningTool(ItemStack itemStack);

    boolean isClock(ItemStack itemStack);

    Optional<Integer> armorBars(ItemStack itemStack);

    Optional<Integer> miningLevel(ItemStack itemStack);

    Optional<Float> miningSpeed(ItemStack itemStack, boolean applyEnchantments);

    Optional<Integer> discSignalStrengt(ItemStack itemStack);

}
