package me.noci.advancedtooltip.core.component;

public interface ComponentRenderer<T> {

    ComponentRenderer<?> FALLBACK = new ComponentRenderer<>() {
        @Override
        public ComponentPrinter render(Object component, Object value) {
            return ComponentPrinter.unsupported(component);
        }

        @Override
        public ComponentPrinter parse(Object value) {
            return null;
        }
    };

    default ComponentPrinter render(Object component, T value) {
        return ComponentPrinter.component(component, parse(value));
    }

    ComponentPrinter parse(T value);

}
