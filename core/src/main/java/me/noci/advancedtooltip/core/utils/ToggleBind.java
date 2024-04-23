package me.noci.advancedtooltip.core.utils;

import me.noci.advancedtooltip.core.TooltipAddon;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;

import java.util.function.Supplier;

public class ToggleBind {

    private final Supplier<Key> keyBind;
    private final Supplier<Boolean> canToggle;

    private boolean toggled = false;

    public ToggleBind(Supplier<Key> key, Supplier<Boolean> canToggle) {
        this.keyBind = key;
        this.canToggle = canToggle;
        Laby.references().eventBus().registerListener(this);
    }

    @Subscribe
    public void onKeyPressed(KeyEvent event) {
        if (!TooltipAddon.enabled()) return;
        if (event.key() != keyBind.get()) return;
        if (event.state() != KeyEvent.State.PRESS) return;
        if (!canToggle.get()) return;

        toggled = !toggled;
    }

    public boolean toggledOr(Supplier<Boolean> or) {
        return toggled || or.get();
    }

    public void toggle(boolean toggled) {
        this.toggled = toggled;
    }
}
