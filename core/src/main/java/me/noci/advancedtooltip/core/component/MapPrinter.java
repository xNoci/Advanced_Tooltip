package me.noci.advancedtooltip.core.component;

import me.noci.advancedtooltip.core.TooltipAddon;
import net.labymod.api.util.I18n;

import java.util.Iterator;
import java.util.Map;

public class MapPrinter<K, V> implements MapComponentPrinter<K, V> {

    private final String name;
    private final Map<K, V> map;
    private final boolean expandable;
    private int indentLevel = 0;
    @SuppressWarnings("unchecked")
    private ValueHandler<String, K> keyHandler = (ValueHandler<String, K>) ValueHandler.DEFAULT;
    @SuppressWarnings("unchecked")
    private ValueHandler<ComponentPrinter, V> valueHandler = (ValueHandler<ComponentPrinter, V>) ValueHandler.DEFAULT;

    protected MapPrinter(String name, Map<K, V> map, boolean expandable) {
        this.name = name;
        this.map = map;
        this.expandable = expandable;
    }

    @Override
    public String print() {
        Iterator<Map.Entry<K, V>> entries = map.entrySet().iterator();
        if (!entries.hasNext()) {
            return name + ": {}";
        }

        boolean expandComponents = TooltipAddon.get().configuration().displayComponent().expandComponents().isPressed();
        if (expandable && !expandComponents) {
            String keyTranslationKey = TooltipAddon.get().configuration().displayComponent().expandComponents().getTranslationKey();
            return name + ": " + I18n.translate("advancedtooltip.components.pressToShowObject", I18n.translate(keyTranslationKey));
        }

        StringBuilder builder = new StringBuilder();
        builder.append(name).append(": {\n");

        while (entries.hasNext()) {
            var entry = entries.next();
            ComponentPrinter value = valueHandler.apply(entry.getValue());
            value.setIndentLevel(indentLevel + 1);

            indent(builder, 1).append("'").append(keyHandler.apply(entry.getKey())).append("': ").append(value.print());
            if (entries.hasNext()) builder.append(",");
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
