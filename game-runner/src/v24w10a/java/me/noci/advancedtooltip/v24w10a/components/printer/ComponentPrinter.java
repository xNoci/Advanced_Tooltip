package me.noci.advancedtooltip.v24w10a.components.printer;

import net.labymod.api.util.I18n;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.nbt.CompoundTag;

import java.util.List;
import java.util.Map;

public interface ComponentPrinter {

    static ComponentPrinter nbt(CompoundTag compoundTag) {
        return nbt(null, compoundTag);
    }

    static ComponentPrinter nbt(String name, CompoundTag compoundTag) {
        return new NbtPrinter(name, compoundTag);
    }

    static <T> ComponentPrinter value(String name, T value) {
        return new ValueComponentPrinter<>(name, value);
    }

    static ComponentPrinter text(String text) {
        return new StringComponentPrinter(text);
    }

    static ComponentPrinter object(String name, ComponentPrinter... values) {
        return object(name, List.of(values));
    }

    static ComponentPrinter object(String name, List<ComponentPrinter> values) {
        return new ObjectPrinter(name, values, false);
    }

    static ComponentPrinter expandableObject(String name, ComponentPrinter... values) {
        return expandableObject(name, List.of(values));
    }

    static ComponentPrinter expandableObject(String name, List<ComponentPrinter> values) {
        return new ObjectPrinter(name, values, true);
    }

    static ComponentPrinter object(ComponentPrinter... values) {
        return object(List.of(values));
    }

    static ComponentPrinter object(List<ComponentPrinter> values) {
        return new ObjectPrinter(null, values, false);
    }

    static ComponentPrinter expandableObject(ComponentPrinter... values) {
        return expandableObject(List.of(values));
    }

    static ComponentPrinter expandableObject(List<ComponentPrinter> values) {
        return new ObjectPrinter(null, values, true);
    }

    static <K, V> MapComponentPrinter<K, V> map(String name, Map<K, V> map) {
        return new MapPrinter<>(name, map, false);
    }

    static <K, V> MapComponentPrinter<K, V> expandableMap(String name, Map<K, V> map) {
        return new MapPrinter<>(name, map, true);
    }

    static <T> ListComponentPrinter<T> list(String name, List<T> values) {
        return new ListPrinter<>(name, values, false);
    }

    static <T> ListComponentPrinter<T> expandableList(String name, List<T> values) {
        return new ListPrinter<>(name, values, true);
    }

    static <T> ComponentPrinter component(TypedDataComponent<T> component, ComponentPrinter printer) {
        return new DataComponentPrinter<>(component, printer);
    }

    static ComponentPrinter unsupported() {
        return UnsupportedComponentPrinter.PRINTER;
    }

    static String print(List<ComponentPrinter> components) {
        int componentCount = components.size();
        String name = I18n.translate("advancedtooltip.components.count", componentCount);
        return list(name, components).handler(ComponentPrinter::print).print();
    }

    String print();

    int indentLevel();

    void setIndentLevel(int indentLevel);

    default StringBuilder indent(StringBuilder builder) {
        return indent(builder, 0);
    }

    default StringBuilder indent(StringBuilder builder, int extraLevel) {
        return builder.append(indentString(extraLevel));
    }

    default String indentString(int extraLevel) {
        return " ".repeat(2 * (indentLevel() + extraLevel));
    }

}
