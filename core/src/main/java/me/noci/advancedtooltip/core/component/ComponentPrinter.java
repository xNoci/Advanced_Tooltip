package me.noci.advancedtooltip.core.component;

import net.labymod.api.util.I18n;

import java.util.List;
import java.util.Map;

public interface ComponentPrinter {

    static ComponentPrinter nbt(Object compoundTag) {
        return nbt(null, compoundTag);
    }

    static ComponentPrinter nbt(String name, Object compoundTag) {
        return new NbtPrinter(name, compoundTag);
    }

    static <T> ComponentPrinter value(String name, T value) {
        return new ValueComponentPrinter<>(name, value);
    }

    static ComponentPrinter text(String text) {
        return new StringComponentPrinter(text);
    }

    static ObjectComponentPrinter object(String name, ComponentPrinter... values) {
        return object(name, List.of(values));
    }

    static ObjectComponentPrinter object(String name, List<ComponentPrinter> values) {
        return new ObjectPrinter(name, values, false);
    }

    static ObjectComponentPrinter expandableObject(String name, ComponentPrinter... values) {
        return expandableObject(name, List.of(values));
    }

    static ObjectComponentPrinter expandableObject(String name, List<ComponentPrinter> values) {
        return new ObjectPrinter(name, values, true);
    }

    static ObjectComponentPrinter object(ComponentPrinter... values) {
        return object(List.of(values));
    }

    static ObjectComponentPrinter object(List<ComponentPrinter> values) {
        return new ObjectPrinter(null, values, false);
    }

    static ObjectComponentPrinter expandableObject(ComponentPrinter... values) {
        return expandableObject(List.of(values));
    }

    static ObjectComponentPrinter expandableObject(List<ComponentPrinter> values) {
        return new ObjectPrinter(null, values, true);
    }

    static <K, V> MapComponentPrinter<K, V> map(String name, Map<K, V> map) {
        return new MapPrinter<>(name, map, false);
    }

    static <K, V> MapComponentPrinter<K, V> expandableMap(String name, Map<K, V> map) {
        return new MapPrinter<>(name, map, true);
    }

    //TODO Add overload list(String name, Iterable<T> values, ValueHandler<T> handler)
    static <T> ListComponentPrinter<T> list(String name, Iterable<T> values) {
        return new ListPrinter<>(name, values, false);
    }

    static <T> ListComponentPrinter<T> expandableList(String name, Iterable<T> values) {
        return new ListPrinter<>(name, values, true);
    }

    static ComponentPrinter component(Object component, ComponentPrinter printer) {
        return new DataComponentPrinter(component, printer);
    }

    static ComponentPrinter unit(Object component) {
        return new UnitComponentPrinter(component);
    }

    static ComponentPrinter unsupported(Object component) {
        return ComponentPrinter.component(component, UnsupportedComponentPrinter.PRINTER);
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
