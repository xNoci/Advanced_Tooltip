package me.noci.advancedtooltip.v24w09a.components.printer;

public class ValueComponentPrinter<T> implements ComponentPrinter {

    private final String name;
    private final T value;

    protected ValueComponentPrinter(String name, T value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String print() {
        return indent(new StringBuilder()).append(name).append("=").append(value).toString();
    }

    @Override
    public int indentLevel() {
        return 0;
    }

    @Override
    public void setIndentLevel(int indentLevel) {

    }
}
