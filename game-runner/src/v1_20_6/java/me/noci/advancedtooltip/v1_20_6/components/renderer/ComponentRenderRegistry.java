package me.noci.advancedtooltip.v1_20_6.components.renderer;

import com.google.common.collect.Maps;
import me.noci.advancedtooltip.core.component.ComponentPrinter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;

import java.util.HashMap;

public class ComponentRenderRegistry {

    private final static ComponentRenderer<?> FALLBACK = ComponentPrinter::unsupported;

    private final HashMap<DataComponentType<?>, ComponentRenderer<?>> componentTypeRenderMap = Maps.newHashMap();

    @SuppressWarnings("unchecked")
    public <T> ComponentPrinter printer(TypedDataComponent<T> component) {
        ComponentRenderer<T> renderer = (ComponentRenderer<T>) componentTypeRenderMap.getOrDefault(component.type(), FALLBACK);
        return renderer.apply(component);
    }

    @SafeVarargs
    public final <T> ComponentRenderRegistry register(ComponentRenderer<T> renderer, DataComponentType<T>... components) {
        for (DataComponentType<T> component : components) {
            try {
                register(component, renderer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    private <T> void register(DataComponentType<T> componentType, ComponentRenderer<T> renderer) {
        if (componentTypeRenderMap.containsKey(componentType))
            throw new IllegalStateException("Failed to register ComponentRenderer: already exists");
        componentTypeRenderMap.put(componentType, renderer);
    }

}
