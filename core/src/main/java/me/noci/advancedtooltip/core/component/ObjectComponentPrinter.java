package me.noci.advancedtooltip.core.component;

public interface ObjectComponentPrinter extends ComponentPrinter {


    ObjectComponentPrinter inline(boolean inline);

    default ObjectComponentPrinter inline() {
        inline(true);
        return this;
    }

}
