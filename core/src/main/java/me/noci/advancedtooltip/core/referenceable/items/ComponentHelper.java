package me.noci.advancedtooltip.core.referenceable.items;

import me.noci.advancedtooltip.core.utils.MapDecoration;
import me.noci.advancedtooltip.core.utils.SignText;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Nullable
@Referenceable
public interface ComponentHelper {

    ComponentHelper DEFAULT = new DefaultComponentHelper();

    @Nullable
    String componentName(Object component);

    @Nullable
    StringBuilder prettyPrintCompound(Object compoundTag, int indentLevel, boolean withNbtArrayData);

    boolean isEmptyCompound(Object compoundTag);

    @Nullable
    String displayItemData(ItemStack itemStack, boolean withNbtArrayData);

    int repairCost(ItemStack itemStack);

    default int anvilUsages(ItemStack itemStack) {
        int repairCost = repairCost(itemStack);
        if (repairCost == 0) return 0;
        return log2(repairCost + 1);
    }

    @Nullable
    List<MapDecoration> mapDecorations(ItemStack itemStack);

    @Nullable
    String commandBlockCommand(ItemStack itemStack);

    @Nullable
    SignText signText(ItemStack itemStack);

    boolean unbreakable(ItemStack itemStack);

    class DefaultComponentHelper implements ComponentHelper {
        @Override
        public @Nullable String componentName(Object component) {
            return null;
        }

        @Override
        public @Nullable StringBuilder prettyPrintCompound(Object compoundTag, int indentLevel, boolean withNbtArrayData) {
            return null;
        }

        @Override
        public boolean isEmptyCompound(Object compoundTag) {
            return true;
        }

        @Override
        public @Nullable String displayItemData(ItemStack itemStack, boolean withNbtArrayData) {
            return null;
        }

        @Override
        public int repairCost(ItemStack itemStack) {
            return 0;
        }

        @Override
        public @Nullable List<MapDecoration> mapDecorations(ItemStack itemStack) {
            return null;
        }

        @Override
        public @Nullable String commandBlockCommand(ItemStack itemStack) {
            return null;
        }

        @Override
        public @Nullable SignText signText(ItemStack itemStack) {
            return null;
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
