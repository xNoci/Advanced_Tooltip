package me.noci.advancedtooltip.v24w10a.components.printer;

public interface ListComponentPrinter<T> extends ComponentPrinter {

    ListComponentPrinter<T> handler(ValueHandler<T> handler);

    @FunctionalInterface
    interface ValueHandler<T> {
        ValueHandler<?> DEFAULT = value -> value != null ? value.toString() : "NULL";

        String apply(T value);
    }

}
