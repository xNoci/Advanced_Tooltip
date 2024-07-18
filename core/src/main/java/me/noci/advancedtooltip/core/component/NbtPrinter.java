package me.noci.advancedtooltip.core.component;

import me.noci.advancedtooltip.core.TooltipAddon;
import me.noci.advancedtooltip.core.referenceable.items.ComponentHelper;
import net.labymod.api.util.I18n;
import org.jetbrains.annotations.Nullable;

public class NbtPrinter implements ComponentPrinter {

    private static final StringBuilder FALLBACK_BUILDER = new StringBuilder("{}");

    private final Object compoundTag;
    @Nullable private final String name;
    private int indentLevel = 0;

    protected NbtPrinter(String name, Object compoundTag) {
        this.name = name == null || name.isBlank() ? null : name;
        this.compoundTag = compoundTag;
    }

    @Override
    public String print() {
        ComponentHelper helper = TooltipAddon.componentHelper();
        var settings = TooltipAddon.get().configuration().displayComponent();
        StringBuilder builder = new StringBuilder();

        if (name != null) {
            builder.append(name).append(": ");
        }

        if (helper.isEmptyCompound(compoundTag)) {
            return builder.append("{}").toString();
        }

        boolean expandComponents = settings.expandComponents().isPressed();
        if (!expandComponents) {
            String keyTranslationKey = settings.expandComponents().getTranslationKey();
            builder.append(I18n.translate("advancedtooltip.components.pressToShowObject", I18n.translate(keyTranslationKey)));
            return builder.toString();
        }

        boolean withNbtArrayData = settings.printWithNbtArrayData().isPressed();
        StringBuilder nbtBuilder = helper.prettyPrintCompound(compoundTag, indentLevel, withNbtArrayData);

        if (nbtBuilder != null) {
            nbtBuilder.delete(0, nbtBuilder.indexOf("{"));
            nbtBuilder.delete(nbtBuilder.indexOf("{") + 1, nbtBuilder.indexOf("\""));
            nbtBuilder.insert(nbtBuilder.indexOf("{") + 1, new StringBuilder("\n").append(indentString(1)));
            nbtBuilder.delete(nbtBuilder.lastIndexOf("\n") + 1, nbtBuilder.lastIndexOf("}"));
            nbtBuilder.insert(nbtBuilder.lastIndexOf("}"), indentString(0));
        } else {
            nbtBuilder = FALLBACK_BUILDER;
        }

        builder.append(nbtBuilder);

        return builder.toString();
    }

    @Override
    public int indentLevel() {
        return indentLevel;
    }

    @Override
    public void setIndentLevel(int indentLevel) {
        this.indentLevel = indentLevel;
    }
}
