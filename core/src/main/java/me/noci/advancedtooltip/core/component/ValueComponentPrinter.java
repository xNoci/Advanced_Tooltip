package me.noci.advancedtooltip.core.component;

public class ValueComponentPrinter<T> implements ComponentPrinter {

    private final String name;
    private final T value;

    protected ValueComponentPrinter(String name, T value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String print() {
        return indentString(0) + name + "=" + value;
    }

    @Override
    public int indentLevel() {
        return 0;
    }

    @Override
    public void setIndentLevel(int indentLevel) {

    }
}
