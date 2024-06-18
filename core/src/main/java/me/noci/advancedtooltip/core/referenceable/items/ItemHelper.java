package me.noci.advancedtooltip.core.referenceable.items;

import me.noci.advancedtooltip.core.utils.CompassTarget;
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
        public boolean isFuel(ItemStack itemStack) {
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

        @Override
        public Optional<Integer> discTickLength(ItemStack itemStack) {
            return Optional.empty();
        }

        @Override
        public int burnDuration(ItemStack itemStack) {
            return 0;
        }

        @Override
        public Optional<CompassTarget> compassTarget(ItemStack itemStack) {
            return Optional.empty();
        }
    };

    boolean isArmor(ItemStack itemStack);

    boolean isMiningTool(ItemStack itemStack);

    boolean isClock(ItemStack itemStack);

    boolean isFuel(ItemStack itemStack);

    Optional<Integer> armorBars(ItemStack itemStack);

    Optional<Integer> miningLevel(ItemStack itemStack);

    Optional<Float> miningSpeed(ItemStack itemStack, boolean applyEnchantments);

    Optional<Integer> discSignalStrengt(ItemStack itemStack);

    Optional<Integer> discTickLength(ItemStack itemStack);

    int burnDuration(ItemStack itemStack);

    Optional<CompassTarget> compassTarget(ItemStack itemStack);
}
