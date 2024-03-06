package me.noci.advancedtooltip.v24w10a.components;

import me.noci.advancedtooltip.core.component.ComponentHelper;
import net.labymod.api.models.Implements;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
@Implements(ComponentHelper.class)
public class VersionedComponentHelper implements ComponentHelper {

    @Override
    public Optional<String> componentName(Object component) {
        if (!(component instanceof TypedDataComponent<?> dataComponent)) return Optional.empty();
        return Optional.of(dataComponent.type().toString());
    }

    @Override
    public Optional<StringBuilder> prettyPrintCompound(Object compoundTag, int indentLevel, boolean withNbtArrayData) {
        if (!(compoundTag instanceof CompoundTag tag)) return Optional.empty();
        StringBuilder builder = new StringBuilder();
        NbtUtils.prettyPrint(builder, tag, indentLevel, withNbtArrayData);
        return Optional.of(builder);
    }

    @Override
    public boolean isEmptyCompound(Object compoundTag) {
        if (!(compoundTag instanceof CompoundTag tag)) return true;
        return tag.isEmpty();
    }
}
