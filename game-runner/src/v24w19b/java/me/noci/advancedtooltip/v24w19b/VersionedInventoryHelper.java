package me.noci.advancedtooltip.v24w19b;

import me.noci.advancedtooltip.core.referenceable.InventoryHelper;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import javax.inject.Singleton;

@Singleton
@Implements(InventoryHelper.class)
public class VersionedInventoryHelper implements InventoryHelper {

    @Override
    public boolean isInventoryOpen() {
        Screen screen = Minecraft.getInstance().screen;
        if (screen == null) return false;
        return screen instanceof AbstractContainerScreen<?>;
    }

}
