package me.noci.advancedtooltip.v24w09a.components.printer;

import net.minecraft.core.component.TypedDataComponent;

public class DataComponentPrinter<T> implements ComponentPrinter {

    private final TypedDataComponent<?> component;
    private final ComponentPrinter componentPrinter;
    private int indentLevel = 0;

    protected DataComponentPrinter(TypedDataComponent<T> component, ComponentPrinter componentPrinter) {
        this.component = component;
        this.componentPrinter = componentPrinter;
    }

    @Override
    public String print() {
        return component.type() + "=>[" + componentPrinter.print() + "]";
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
