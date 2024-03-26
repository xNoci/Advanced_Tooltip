package me.noci.advancedtooltip.core.component;

import me.noci.advancedtooltip.core.TooltipAddon;

public class UnitComponentPrinter implements ComponentPrinter {

    private final String componentName;

    protected UnitComponentPrinter(Object component) {
        this.componentName = TooltipAddon.componentHelper().componentName(component).orElse("UNKNOWN");
    }

    @Override
    public String print() {
        return componentName;
    }

    @Override
    public int indentLevel() {
        return 0;
    }

    @Override
    public void setIndentLevel(int indentLevel) {

    }

}
