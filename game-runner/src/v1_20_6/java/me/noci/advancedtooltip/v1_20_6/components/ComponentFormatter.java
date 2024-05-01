package me.noci.advancedtooltip.v1_20_6.components;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ComponentFormatter {

    @Nullable private ComponentPrinter printer;

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public final <T> ComponentApplier<T> forComponents(TypedDataComponent<?> component, DataComponentType<T>... types) {
        if (printer != null) {
            return (ComponentApplier<T>) ComponentApplier.EMPTY_APPLIER;
        }

        if (Arrays.stream(types).noneMatch(componentType -> component.type() == componentType)) {
            return (ComponentApplier<T>) ComponentApplier.EMPTY_APPLIER;
        }

        return (ComponentApplier<T>) new DefaultComponentApplier<>(this, component, component.value());
    }

    private void setPrinter(@Nullable ComponentPrinter printer) {
        this.printer = printer;
    }

    public ComponentPrinter getOr(ComponentPrinter defaultPrinter) {
        return printer != null ? printer : defaultPrinter;
    }

    @FunctionalInterface
    public interface ComponentApplier<T> {
        ComponentApplier<?> EMPTY_APPLIER = (function, raw) -> {
        };

        void apply(BiFunction<T, TypedDataComponent<?>, ComponentPrinter> function, boolean raw);

        default void handle(Function<T, ComponentPrinter> function) {
            apply((value, component) -> function.apply(value), false);
        }

        default void handleRaw(BiFunction<T, TypedDataComponent<?>, ComponentPrinter> function) {
            apply(function, true);
        }

        @SuppressWarnings("unchecked")
        default <E> void handleAs(Class<E> ignoredType, Function<E, ComponentPrinter> function) {
            handle(value -> function.apply((E) value));
        }
    }

    public static class DefaultComponentApplier<T> implements ComponentApplier<T> {

        private final ComponentFormatter holder;
        private final TypedDataComponent<?> component;
        private final T value;

        public DefaultComponentApplier(ComponentFormatter holder, TypedDataComponent<?> component, T value) {
            this.holder = holder;
            this.component = component;
            this.value = value;
        }

        @Override
        public void apply(BiFunction<T, TypedDataComponent<?>, ComponentPrinter> function, boolean raw) {
            ComponentPrinter printer = function.apply(value, component);
            if (printer != null) {
                printer = raw ? printer : ComponentPrinter.component(component, printer);
            }
            holder.setPrinter(printer);
        }
    }

}
