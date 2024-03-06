package me.noci.advancedtooltip.v1_8_9;

import me.noci.advancedtooltip.core.referenceable.InventoryHelper;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;

import javax.inject.Singleton;

@Singleton
@Implements(InventoryHelper.class)
public class VersionedInventoryHelper implements InventoryHelper {

    @Override
    public boolean isInventoryOpen() {
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if (screen == null) return false;
        return screen instanceof GuiContainer;
    }

}
