package me.noci.advancedtooltip.core.component;

public class StringComponentPrinter implements ComponentPrinter {

    private final String text;

    protected StringComponentPrinter(String text) {
        this.text = text;
    }

    @Override
    public String print() {
        return indentString(0) + text;
    }

    @Override
    public int indentLevel() {
        return 0;
    }

    @Override
    public void setIndentLevel(int indentLevel) {

    }
}
