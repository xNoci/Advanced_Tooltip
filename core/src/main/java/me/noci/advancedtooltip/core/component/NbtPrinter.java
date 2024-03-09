package me.noci.advancedtooltip.core.component;

import me.noci.advancedtooltip.core.TooltipAddon;
import me.noci.advancedtooltip.core.config.DeveloperSubSetting;
import me.noci.advancedtooltip.core.referenceable.items.ComponentHelper;
import net.labymod.api.util.I18n;

import java.util.Optional;

public class NbtPrinter implements ComponentPrinter {

    private static final StringBuilder FALLBACK_BUILDER = new StringBuilder("{}");

    private final Object compoundTag;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") private final Optional<String> name;
    private int indentLevel = 0;

    protected NbtPrinter(String name, Object compoundTag) {
        this.name = Optional.ofNullable(name).map(s -> s.isBlank() ? null : s);
        this.compoundTag = compoundTag;
    }

    @Override
    public String print() {
        ComponentHelper helper = TooltipAddon.componentHelper();
        var settings = TooltipAddon.get().configuration().developerSettings().displayComponent();
        StringBuilder builder = new StringBuilder();

        name.ifPresent(s -> builder.append(s).append(": "));
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
        Optional<StringBuilder> builderOptional = helper.prettyPrintCompound(compoundTag, indentLevel, withNbtArrayData);

        builderOptional.ifPresent(nbtBuilder -> {
            nbtBuilder.delete(0, nbtBuilder.indexOf("{"));
            nbtBuilder.delete(nbtBuilder.indexOf("{") + 1, nbtBuilder.indexOf("\""));
            nbtBuilder.insert(nbtBuilder.indexOf("{") + 1, new StringBuilder("\n").append(indentString(1)));
            nbtBuilder.delete(nbtBuilder.lastIndexOf("\n") + 1, nbtBuilder.lastIndexOf("}"));
            nbtBuilder.insert(nbtBuilder.lastIndexOf("}"), indentString(0));
        });

        builder.append(builderOptional.orElse(FALLBACK_BUILDER));

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
