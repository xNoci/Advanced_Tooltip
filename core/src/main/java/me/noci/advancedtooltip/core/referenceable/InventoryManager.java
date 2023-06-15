package me.noci.advancedtooltip.core.referenceable;

import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public interface InventoryManager {

    boolean isInventoryOpen();

}
