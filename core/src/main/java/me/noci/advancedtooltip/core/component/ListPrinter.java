package me.noci.advancedtooltip.core.component;

import me.noci.advancedtooltip.core.TooltipAddon;
import net.labymod.api.util.I18n;
import net.labymod.api.util.Streams;

import java.util.Iterator;

public class ListPrinter<T> implements ListComponentPrinter<T> {

    private final String name;
    private final Iterable<T> values;
    private final boolean expandable;
    private int indentLevel = 0;
    @SuppressWarnings("unchecked") private ValueHandler<T> handler = (ValueHandler<T>) ValueHandler.DEFAULT;

    protected ListPrinter(String name, Iterable<T> values, boolean expandable) {
        this.name = name;
        this.values = values;
        this.expandable = expandable;
        updateIndentLevel();
    }

    @Override
    public String print() {
        Iterator<T> iterator = values.iterator();

        if (!iterator.hasNext()) {
            return name + ": []";
        }

        boolean expandComponents = TooltipAddon.get().configuration().displayComponent().expandComponents().isPressed();
        if (expandable && !expandComponents) {
            String keyTranslationKey = TooltipAddon.get().configuration().displayComponent().expandComponents().getTranslationKey();
            return name + ": " + I18n.translate("advancedtooltip.components.pressToShowList", I18n.translate(keyTranslationKey));
        }

        StringBuilder builder = new StringBuilder();
        builder.append(name).append(": [\n");

        while (iterator.hasNext()) {
            T value = iterator.next();
            indent(builder, 1).append(handler.apply(value));
            if (iterator.hasNext()) builder.append(",");
            builder.append("\n");
        }

        indent(builder).append("]");
        return builder.toString();
    }

    @Override
    public int indentLevel() {
        return indentLevel;
    }

    @Override
    public void setIndentLevel(int indentLevel) {
        this.indentLevel = indentLevel;
        updateIndentLevel();
    }

    private void updateIndentLevel() {
        Streams.stream(values).filter(value -> value instanceof ComponentPrinter).forEach(component -> ((ComponentPrinter) component).setIndentLevel(indentLevel + 1));
    }

    @Override
    public ListComponentPrinter<T> handler(ValueHandler<T> handler) {
        if (handler == null) throw new IllegalArgumentException("Handler cannot be null");
        this.handler = handler;
        return this;
    }
}
