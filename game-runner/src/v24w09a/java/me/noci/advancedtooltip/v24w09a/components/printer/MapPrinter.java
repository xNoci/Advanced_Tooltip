package me.noci.advancedtooltip.v24w09a.components.printer;

import com.google.common.collect.Lists;
import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import net.labymod.api.util.I18n;

import java.util.List;
import java.util.Map;

public class MapPrinter<K, V> implements MapComponentPrinter<K, V> {

    private final String name;
    private final Map<K, V> map;
    private final boolean expandable;
    private int indentLevel = 0;
    @SuppressWarnings("unchecked") private ValueHandler<String, K> keyHandler = (ValueHandler<String, K>) ValueHandler.DEFAULT;
    @SuppressWarnings("unchecked") private ValueHandler<ComponentPrinter, V> valueHandler = (ValueHandler<ComponentPrinter, V>) ValueHandler.DEFAULT;

    protected MapPrinter(String name, Map<K, V> map, boolean expandable) {
        this.name = name;
        this.map = map;
        this.expandable = expandable;
    }

    @Override
    public String print() {
        List<Map.Entry<K, V>> entries = Lists.newArrayList(map.entrySet());
        if (entries.isEmpty()) {
            return name + ": {}";
        }

        boolean expandComponents = AdvancedTooltipAddon.getInstance().configuration().developerSettings().expandComponents().get().isPressed();
        if (expandable && !expandComponents) {
            String keyTranslationKey = AdvancedTooltipAddon.getInstance().configuration().developerSettings().expandComponents().get().getTranslationKey();
            return name + ": " + I18n.translate("advancedtooltip.components.pressToShowObject", I18n.translate(keyTranslationKey));
        }

        StringBuilder builder = new StringBuilder();
        builder.append(name).append(": {\n");

        for (int i = 0; i < entries.size(); i++) {
            var entry = entries.get(i);

            ComponentPrinter value = valueHandler.apply(entry.getValue());
            value.setIndentLevel(indentLevel + 1);

            indent(builder, 1).append("'").append(keyHandler.apply(entry.getKey())).append("': ").append(value.print());
            if (i + 1 != entries.size()) builder.append(",");
            builder.append("\n");
        }

        indent(builder).append("}");
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

    @Override
    public MapComponentPrinter<K, V> handler(ValueHandler<String, K> keyHandler, ValueHandler<ComponentPrinter, V> valueHandler) {
        if (keyHandler == null) throw new IllegalArgumentException("Key handler cannot be null");
        if (valueHandler == null) throw new IllegalArgumentException("Value handler cannot be null");
        this.keyHandler = keyHandler;
        this.valueHandler = valueHandler;
        return this;
    }
}
