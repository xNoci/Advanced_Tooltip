package me.noci.advancedtooltip.core;

import me.noci.advancedtooltip.core.config.TooltipConfiguration;
import me.noci.advancedtooltip.core.generated.DefaultReferenceStorage;
import me.noci.advancedtooltip.core.listener.ItemStackTooltipListener;
import me.noci.advancedtooltip.core.referenceable.InventoryHelper;
import me.noci.advancedtooltip.core.referenceable.items.ComponentHelper;
import me.noci.advancedtooltip.core.referenceable.items.FoodItems;
import me.noci.advancedtooltip.core.referenceable.items.ItemHelper;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.revision.SimpleRevision;
import net.labymod.api.util.version.SemanticVersion;

@AddonMain
public class TooltipAddon extends LabyAddon<TooltipConfiguration> {

    private static TooltipAddon instance;

    public static TooltipAddon get() {
        return instance;
    }

    public static boolean enabled() {
        return instance.configuration().enabled().get();
    }

    public static FoodItems foodItems() {
        return instance.foodItems;
    }

    public static ItemHelper itemHelper() {
        return instance.itemHelper;
    }

    public static InventoryHelper inventoryHelper() {
        return instance.inventoryHelper;
    }

    public static ComponentHelper componentHelper() {
        return instance.componentHelper;
    }

    private FoodItems foodItems = FoodItems.DEFAULT;
    private ItemHelper itemHelper = ItemHelper.DEFAULT;
    private InventoryHelper inventoryHelper = InventoryHelper.DEFAULT;
    private ComponentHelper componentHelper = ComponentHelper.DEFAULT;

    public TooltipAddon() {
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
        if (foodItems != null) {
            this.foodItems = foodItems;
        }

        ItemHelper itemHelper = referenceStorage.getItemHelper();
        if (itemHelper != null) {
            this.itemHelper = itemHelper;
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
        this.registerListener(new ItemStackTooltipListener(this, this.foodItems, this.itemHelper, this.componentHelper));
    }

    @Override
    protected Class<TooltipConfiguration> configurationClass() {
        return TooltipConfiguration.class;
    }
}
