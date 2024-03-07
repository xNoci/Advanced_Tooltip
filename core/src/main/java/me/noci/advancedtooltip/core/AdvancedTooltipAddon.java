package me.noci.advancedtooltip.core;

import lombok.Getter;
import me.noci.advancedtooltip.core.config.AdvancedTooltipConfiguration;
import me.noci.advancedtooltip.core.generated.DefaultReferenceStorage;
import me.noci.advancedtooltip.core.listener.FoodItemTooltipDebugListener;
import me.noci.advancedtooltip.core.listener.ItemStackTooltipListener;
import me.noci.advancedtooltip.core.listener.KeyPressListener;
import me.noci.advancedtooltip.core.component.ComponentHelper;
import me.noci.advancedtooltip.core.referenceable.items.DefaultItemQuery;
import me.noci.advancedtooltip.core.referenceable.InventoryHelper;
import me.noci.advancedtooltip.core.referenceable.items.FoodItems;
import me.noci.advancedtooltip.core.referenceable.items.ItemQuery;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.revision.SimpleRevision;
import net.labymod.api.util.version.SemanticVersion;

@AddonMain
public class AdvancedTooltipAddon extends LabyAddon<AdvancedTooltipConfiguration> {

    @Getter private static AdvancedTooltipAddon instance;

    public static boolean enabled() {
        return getInstance().configuration().enabled().get();
    }

    @Getter private FoodItems foodItems = FoodItems.DEFAULT;
    @Getter private ItemQuery itemQuery = new DefaultItemQuery();
    @Getter private InventoryHelper inventoryHelper = InventoryHelper.DEFAULT;
    @Getter private ComponentHelper componentHelper = ComponentHelper.DEFAULT;

    public AdvancedTooltipAddon() {
        instance = this;
    }

    @Override
    protected void preConfigurationLoad() {
        Laby.references().revisionRegistry().register(new SimpleRevision("advancedtooltip", new SemanticVersion("1.5.0"), "2023-10-13"));
    }

    @Override
    protected void enable() {
        initialiseReferences();
        registerSettingCategory();
        registerListener();
    }

    private void initialiseReferences() {
        DefaultReferenceStorage referenceStorage = this.referenceStorageAccessor();

        FoodItems foodItems = referenceStorage.getFoodItems();
        if(itemQuery != null) {
            this.foodItems = foodItems;
        }

        ItemQuery itemQuery = referenceStorage.getItemQuery();
        if (itemQuery != null) {
            this.itemQuery = itemQuery;
        }

        InventoryHelper inventoryManager = referenceStorage.getInventoryHelper();
        if (inventoryManager != null) {
            this.inventoryHelper = inventoryManager;
        }

        ComponentHelper componentHelper = referenceStorage.getComponentHelper();
        if (componentHelper != null) {
            this.componentHelper = componentHelper;
        }
    }

    private void registerListener() {
        this.registerListener(new FoodItemTooltipDebugListener(this, this.foodItems));
        this.registerListener(new ItemStackTooltipListener(this, this.foodItems, this.itemQuery));
        this.registerListener(new KeyPressListener(this, this.inventoryHelper));
    }

    @Override
    protected Class<AdvancedTooltipConfiguration> configurationClass() {
        return AdvancedTooltipConfiguration.class;
    }
}
