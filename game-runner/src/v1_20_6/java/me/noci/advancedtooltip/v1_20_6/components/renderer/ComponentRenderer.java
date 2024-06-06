package me.noci.advancedtooltip.v1_20_6.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import net.minecraft.core.component.TypedDataComponent;

import java.util.function.Function;

@FunctionalInterface
public interface ComponentRenderer<T> {
    static <T> ComponentRenderer<T> value(Function<T, ComponentPrinter> renderer) {
        return component -> ComponentPrinter.component(component, renderer.apply(component.value()));
    }

    @SuppressWarnings("unchecked")
    static <T, C> ComponentRenderer<T> cast(Class<C> cast, Function<C, ComponentPrinter> renderer) {
        return value(value -> renderer.apply((C) value));
    }

    ComponentPrinter apply(TypedDataComponent<T> component);

}
