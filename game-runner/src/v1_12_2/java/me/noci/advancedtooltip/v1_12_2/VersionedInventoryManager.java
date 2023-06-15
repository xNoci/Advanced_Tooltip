package me.noci.advancedtooltip.v1_12_2;

import me.noci.advancedtooltip.core.referenceable.InventoryManager;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;

import javax.inject.Singleton;

@Singleton
@Implements(InventoryManager.class)
public class VersionedInventoryManager implements InventoryManager {

    @Override
    public boolean isInventoryOpen() {
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if (screen == null) return false;
        return screen instanceof GuiContainer;
    }

}
