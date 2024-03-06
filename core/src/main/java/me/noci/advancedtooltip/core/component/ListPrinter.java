package me.noci.advancedtooltip.core.component;

import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import net.labymod.api.util.I18n;

import java.util.List;

public class ListPrinter<T> implements ListComponentPrinter<T> {

    private final String name;
    private final List<T> values;
    private final boolean expandable;
    private int indentLevel = 0;
    @SuppressWarnings("unchecked") private ValueHandler<T> handler = (ValueHandler<T>) ValueHandler.DEFAULT;

    protected ListPrinter(String name, List<T> values, boolean expandable) {
        this.name = name;
        this.values = values;
        this.expandable = expandable;
        updateIndentLevel();
    }

    @Override
    public String print() {
        if (values.isEmpty()) {
            return name + ": []";
        }

        boolean expandComponents = AdvancedTooltipAddon.getInstance().configuration().developerSettings().expandComponents().get().isPressed();
        if (expandable && !expandComponents) {
            String keyTranslationKey = AdvancedTooltipAddon.getInstance().configuration().developerSettings().expandComponents().get().getTranslationKey();
            return name + ": " + I18n.translate("advancedtooltip.components.pressToShowList", I18n.translate(keyTranslationKey));
        }

        StringBuilder builder = new StringBuilder();
        builder.append(name).append(": [\n");

        for (int i = 0; i < values.size(); i++) {
            T value = values.get(i);
            indent(builder, 1).append(handler.apply(value));
            if (i + 1 != values.size()) builder.append(",");
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
        values.stream().filter(value -> value instanceof ComponentPrinter).forEach(component -> ((ComponentPrinter) component).setIndentLevel(indentLevel + 1));
    }

    @Override
    public ListComponentPrinter<T> handler(ValueHandler<T> handler) {
        if (handler == null) throw new IllegalArgumentException("Handler cannot be null");
        this.handler = handler;
        return this;
    }
}
