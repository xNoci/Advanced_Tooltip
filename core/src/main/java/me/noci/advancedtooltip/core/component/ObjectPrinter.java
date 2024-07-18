package me.noci.advancedtooltip.core.component;

import me.noci.advancedtooltip.core.TooltipAddon;
import net.labymod.api.util.I18n;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class ObjectPrinter implements ObjectComponentPrinter {

    private final List<ComponentPrinter> printers;
    private final boolean expandable;
    @Nullable private final String name;
    private int indentLevel = 0;
    private boolean inline = false;

    protected ObjectPrinter(String name, List<ComponentPrinter> printers, boolean expandable) {
        this.name = name == null || name.isBlank() ? null : name;
        this.printers = printers;
        this.expandable = expandable;
        updateIndentLevel();
    }

    @Override
    public String print() {
        StringBuilder builder = new StringBuilder();

        if (name != null) {
            builder.append(name).append(": ");
        }

        Iterator<ComponentPrinter> printers = this.printers.iterator();
        if (!printers.hasNext()) {
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

        while (printers.hasNext()) {
            if (!inline) indent(builder, 1);
            builder.append(printers.next().print());
            if (printers.hasNext()) builder.append(",");
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
        int newIndentLevel = indentLevel + 1;
        for (ComponentPrinter printer : printers) {
            printer.setIndentLevel(newIndentLevel);
        }
    }

}
