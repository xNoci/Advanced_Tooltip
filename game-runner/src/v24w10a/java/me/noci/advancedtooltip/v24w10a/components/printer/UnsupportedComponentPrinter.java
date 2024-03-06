package me.noci.advancedtooltip.v24w10a.components.printer;

import net.labymod.api.util.I18n;

public class UnsupportedComponentPrinter implements ComponentPrinter {

    protected static ComponentPrinter PRINTER = new UnsupportedComponentPrinter();

    private UnsupportedComponentPrinter() {
    }

    @Override
    public String print() {
        return I18n.translate("advancedtooltip.components.currentlyNotSupported");
    }

    @Override
    public int indentLevel() {
        return 0;
    }

    @Override
    public void setIndentLevel(int indentLevel) {

    }
}
