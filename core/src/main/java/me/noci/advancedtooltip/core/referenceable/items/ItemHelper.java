package me.noci.advancedtooltip.core.referenceable.items;

import me.noci.advancedtooltip.core.utils.CompassTarget;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.Nullable;

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
        public int armorBars(ItemStack itemStack) {
            return 0;
        }

        @Override
        public int miningLevel(ItemStack itemStack) {
            return 0;
        }

        @Override
        public float miningSpeed(ItemStack itemStack, boolean applyEnchantments) {
            return 0;
        }

        @Override
        public int discSignalStrengt(ItemStack itemStack) {
            return 0;
        }

        @Override
        public int discTickLength(ItemStack itemStack) {
            return 0;
        }

        @Override
        public int burnDuration(ItemStack itemStack) {
            return 0;
        }

        @Override
        public @Nullable CompassTarget compassTarget(ItemStack itemStack) {
            return null;
        }
    };

    boolean isArmor(ItemStack itemStack);

    boolean isMiningTool(ItemStack itemStack);

    boolean isClock(ItemStack itemStack);

    boolean isFuel(ItemStack itemStack);

    int armorBars(ItemStack itemStack);

    int miningLevel(ItemStack itemStack);

    float miningSpeed(ItemStack itemStack, boolean applyEnchantments);

    int discSignalStrengt(ItemStack itemStack);

    int discTickLength(ItemStack itemStack);

    int burnDuration(ItemStack itemStack);

    @Nullable
    CompassTarget compassTarget(ItemStack itemStack);
}
