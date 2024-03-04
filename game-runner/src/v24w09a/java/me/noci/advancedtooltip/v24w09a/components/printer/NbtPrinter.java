package me.noci.advancedtooltip.v24w09a.components.printer;

import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import me.noci.advancedtooltip.core.config.DeveloperSubSetting;
import net.labymod.api.util.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;

public class NbtPrinter implements ComponentPrinter {

    private final CompoundTag compoundTag;
    private int indentLevel = 0;

    protected NbtPrinter(CompoundTag compoundTag) {
        this.compoundTag = compoundTag;
    }

    @Override
    public String print() {
        DeveloperSubSetting settings = AdvancedTooltipAddon.getInstance().configuration().developerSettings();
        StringBuilder builder = new StringBuilder();

        if (!compoundTag.isEmpty()) {
            boolean expandComponents = settings.expandComponents().get().isPressed();
            if (!expandComponents) {
                String keyTranslationKey = AdvancedTooltipAddon.getInstance().configuration().developerSettings().expandComponents().get().getTranslationKey();
                builder.append(I18n.translate("advancedtooltip.components.pressToShowObject", I18n.translate(keyTranslationKey)));
            } else {
                boolean withNbtArrayData = settings.printWithNbtArrayData().get().isPressed();

                StringBuilder nbtBuilder = new StringBuilder();
                NbtUtils.prettyPrint(nbtBuilder, compoundTag, indentLevel, withNbtArrayData);
                nbtBuilder.delete(0, nbtBuilder.indexOf("{"));
                nbtBuilder.delete(nbtBuilder.indexOf("{") + 1, nbtBuilder.indexOf("\""));
                nbtBuilder.insert(nbtBuilder.indexOf("{") + 1, new StringBuilder("\n").append(indentString(1)));
                nbtBuilder.delete(nbtBuilder.lastIndexOf("\n") + 1, nbtBuilder.lastIndexOf("}"));
                nbtBuilder.insert(nbtBuilder.lastIndexOf("}"), indentString(0));

                builder.append(nbtBuilder);
            }
        } else {
            builder.append("{}");
        }

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
