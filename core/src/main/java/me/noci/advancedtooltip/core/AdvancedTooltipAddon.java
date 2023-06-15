package me.noci.advancedtooltip.core;

import lombok.Getter;
import me.noci.advancedtooltip.core.config.AdvancedTooltipConfiguration;
import me.noci.advancedtooltip.core.generated.DefaultReferenceStorage;
import me.noci.advancedtooltip.core.listener.FoodItemTooltipDebugListener;
import me.noci.advancedtooltip.core.listener.ItemStackTooltipListener;
import me.noci.advancedtooltip.core.listener.KeyPressListener;
import me.noci.advancedtooltip.core.referenceable.DefaultInventoryManager;
import me.noci.advancedtooltip.core.referenceable.DefaultItemQuery;
import me.noci.advancedtooltip.core.referenceable.InventoryManager;
import me.noci.advancedtooltip.core.referenceable.ItemQuery;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class AdvancedTooltipAddon extends LabyAddon<AdvancedTooltipConfiguration> {

    private static AdvancedTooltipAddon instance;

    public static AdvancedTooltipAddon getInstance() {
        return instance;
    }

    public static boolean enabled() {
        return getInstance().configuration().enabled().get();
    }

    @Getter private ItemQuery itemQuery = new DefaultItemQuery();
    @Getter private InventoryManager inventoryManager = new DefaultInventoryManager();

    public AdvancedTooltipAddon() {
        instance = this;
    }

    @Override
    protected void enable() {
        initialiseReferences();
        registerSettingCategory();
        registerListener();
    }

    private void initialiseReferences() {
        DefaultReferenceStorage referenceStorage = this.referenceStorageAccessor();

        ItemQuery itemQuery = referenceStorage.getItemQuery();
        if (itemQuery != null) {
            this.itemQuery = itemQuery;
        }

        InventoryManager inventoryManager = referenceStorage.getInventoryManager();
        if (inventoryManager != null) {
            this.inventoryManager = inventoryManager;
        }
    }

    private void registerListener() {
        if (Laby.labyAPI().labyModLoader().isAddonDevelopmentEnvironment()) {
            this.registerListener(new FoodItemTooltipDebugListener(this, this.itemQuery));
        }

        this.registerListener(new ItemStackTooltipListener(this, this.itemQuery));
        this.registerListener(new KeyPressListener(this, this.inventoryManager));
    }

    @Override
    protected Class<AdvancedTooltipConfiguration> configurationClass() {
        return AdvancedTooltipConfiguration.class;
    }
}
