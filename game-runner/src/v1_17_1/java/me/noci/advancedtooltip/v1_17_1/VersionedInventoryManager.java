package me.noci.advancedtooltip.v1_17_1;

import me.noci.advancedtooltip.core.referenceable.InventoryManager;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import javax.inject.Singleton;

@Singleton
@Implements(InventoryManager.class)
public class VersionedInventoryManager implements InventoryManager {

    @Override
    public boolean isInventoryOpen() {
        Screen screen = Minecraft.getInstance().screen;
        if(screen == null) return false;
        return screen instanceof AbstractContainerScreen<?>;
    }

}
