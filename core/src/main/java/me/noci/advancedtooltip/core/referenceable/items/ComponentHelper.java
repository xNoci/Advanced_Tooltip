package me.noci.advancedtooltip.core.referenceable.items;

import me.noci.advancedtooltip.core.utils.MapDecoration;
import me.noci.advancedtooltip.core.utils.SignText;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@Nullable
@Referenceable
public interface ComponentHelper {

    ComponentHelper DEFAULT = new DefaultComponentHelper();

    Optional<String> componentName(Object component);

    Optional<StringBuilder> prettyPrintCompound(Object compoundTag, int indentLevel, boolean withNbtArrayData);

    boolean isEmptyCompound(Object compoundTag);

    Optional<String> displayItemData(ItemStack itemStack, boolean withNbtArrayData);

    Optional<Integer> repairCost(ItemStack itemStack);

    default Optional<Integer> anvilUsages(ItemStack itemStack) {
        return repairCost(itemStack).map(integer -> log2(integer + 1));
    }

    Optional<List<MapDecoration>> mapDecorations(ItemStack itemStack);

    Optional<String> commandBlockCommand(ItemStack itemStack);

    Optional<SignText> signText(ItemStack itemStack);

    boolean unbreakable(ItemStack itemStack);

    class DefaultComponentHelper implements ComponentHelper {
        @Override
        public Optional<String> componentName(Object component) {
            return Optional.empty();
        }

        @Override
        public Optional<StringBuilder> prettyPrintCompound(Object compoundTag, int indentLevel, boolean withNbtArrayData) {
            return Optional.empty();
        }

        @Override
        public boolean isEmptyCompound(Object compoundTag) {
            return true;
        }

        @Override
        public Optional<String> displayItemData(ItemStack itemStack, boolean withNbtArrayData) {
            return Optional.empty();
        }

        @Override
        public Optional<Integer> repairCost(ItemStack itemStack) {
            return Optional.empty();
        }

        @Override
        public Optional<List<MapDecoration>> mapDecorations(ItemStack itemStack) {
            return Optional.empty();
        }

        @Override
        public Optional<String> commandBlockCommand(ItemStack itemStack) {
            return Optional.empty();
        }

        @Override
        public Optional<SignText> signText(ItemStack itemStack) {
            return Optional.empty();
        }

        @Override
        public boolean unbreakable(ItemStack itemStack) {
            return false;
        }
    }

    //-------- Utilities --------

    //https://stackoverflow.com/a/3305400
    private static int log2(int x) {
        return (int) (Math.log(x) / Math.log(2) + 1e-10);
    }

}
