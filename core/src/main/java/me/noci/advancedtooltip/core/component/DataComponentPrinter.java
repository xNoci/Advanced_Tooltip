package me.noci.advancedtooltip.core.component;

import me.noci.advancedtooltip.core.TooltipAddon;

public class DataComponentPrinter implements ComponentPrinter {

    private final String componentName;
    private final ComponentPrinter componentPrinter;
    private int indentLevel = 0;

    protected DataComponentPrinter(Object component, ComponentPrinter componentPrinter) {
        this.componentName = TooltipAddon.componentHelper().componentName(component).orElse("UNKNOWN");
        this.componentPrinter = componentPrinter;
    }

    @Override
    public String print() {
        return componentName + "=>[" + componentPrinter.print() + "]";
    }

    @Override
    public int indentLevel() {
        return indentLevel;
    }

    @Override
    public void setIndentLevel(int indentLevel) {
        this.indentLevel = indentLevel;
        updateIndentLevel();
    }

    private void updateIndentLevel() {
        this.componentPrinter.setIndentLevel(indentLevel());
    }
}
