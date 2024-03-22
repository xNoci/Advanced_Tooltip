package me.noci.advancedtooltip.core.component;

import me.noci.advancedtooltip.core.TooltipAddon;
import net.labymod.api.util.I18n;

import java.util.List;
import java.util.Optional;

public class ObjectPrinter implements ObjectComponentPrinter {

    private final static ListComponentPrinter.ValueHandler<ComponentPrinter> HANDLER = value -> value != null ? value.print() : "NULL";

    private final List<ComponentPrinter> values;
    private final boolean expandable;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") private final Optional<String> name;
    private int indentLevel = 0;
    private boolean inline = false;

    protected ObjectPrinter(String name, List<ComponentPrinter> values, boolean expandable) {
        this.name = Optional.ofNullable(name).map(s -> s.isBlank() ? null : s);
        this.values = values;
        this.expandable = expandable;
        updateIndentLevel();
    }

    @Override
    public String print() {
        StringBuilder builder = new StringBuilder();
        name.ifPresent(s -> builder.append(s).append(": "));
        if (values.isEmpty()) {
            builder.append("{}");
            return builder.toString();
        }

        boolean expandComponents = TooltipAddon.get().configuration().displayComponent().expandComponents().isPressed();
        if (expandable && !expandComponents) {
            String keyTranslationKey = TooltipAddon.get().configuration().displayComponent().expandComponents().getTranslationKey();
            builder.append(I18n.translate("advancedtooltip.components.pressToShowObject", I18n.translate(keyTranslationKey)));
            return builder.toString();
        }

        builder.append("{");
        if (!inline) builder.append("\n");
        for (int i = 0; i < values.size(); i++) {
            ComponentPrinter value = values.get(i);
            if (!inline) indent(builder, 1);
            builder.append(HANDLER.apply(value));
            if (i + 1 != values.size()) builder.append(",");
            if (!inline) builder.append("\n");
        }

        if (!inline) indent(builder);
        builder.append("}");
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

    @Override
    public ObjectComponentPrinter inline(boolean inline) {
        this.inline = inline;
        return this;
    }

    private void updateIndentLevel() {
        values.forEach(component -> component.setIndentLevel(indentLevel + 1));
    }

}
