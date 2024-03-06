package me.noci.advancedtooltip.core.component;

import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@Nullable
@Referenceable
public interface ComponentHelper {

    ComponentHelper DEFAULT = new ComponentHelper() {
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
    };

    Optional<String> componentName(Object component);

    Optional<StringBuilder> prettyPrintCompound(Object compoundTag, int indentLevel, boolean withNbtArrayData);

    boolean isEmptyCompound(Object compoundTag);

}
