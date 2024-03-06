package me.noci.advancedtooltip.core.component;

public interface MapComponentPrinter<K, V> extends ComponentPrinter {

    MapComponentPrinter<K, V> handler(ValueHandler<String, K> keyHandler, ValueHandler<ComponentPrinter, V> valueHandler);

    @FunctionalInterface
    interface ValueHandler<R, T> {
        ValueHandler<?, ?> DEFAULT = value -> value != null ? value.toString() : "NULL";

        R apply(T value);
    }

}
