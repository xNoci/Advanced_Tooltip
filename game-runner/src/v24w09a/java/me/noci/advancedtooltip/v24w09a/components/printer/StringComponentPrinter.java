package me.noci.advancedtooltip.v24w09a.components.printer;

public class StringComponentPrinter implements ComponentPrinter {

    private final String text;

    protected StringComponentPrinter(String text) {
        this.text = text;
    }

    @Override
    public String print() {
        return indent(new StringBuilder()).append(text).toString();
    }

    @Override
    public int indentLevel() {
        return 0;
    }

    @Override
    public void setIndentLevel(int indentLevel) {

    }
}
