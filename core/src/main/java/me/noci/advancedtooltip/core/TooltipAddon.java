package me.noci.advancedtooltip.core;

import lombok.Setter;
import me.noci.advancedtooltip.core.config.TooltipConfiguration;
import me.noci.advancedtooltip.core.generated.DefaultReferenceStorage;
import me.noci.advancedtooltip.core.listener.ItemStackTooltipListener;
import me.noci.advancedtooltip.core.referenceable.InventoryHelper;
import me.noci.advancedtooltip.core.referenceable.TickManager;
import me.noci.advancedtooltip.core.referenceable.items.ComponentHelper;
import me.noci.advancedtooltip.core.referenceable.items.FoodItems;
import me.noci.advancedtooltip.core.referenceable.items.ItemHelper;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.revision.SimpleRevision;
import net.labymod.api.util.version.SemanticVersion;

import java.util.function.Consumer;

@AddonMain
public class TooltipAddon extends LabyAddon<TooltipConfiguration> {

    private static TooltipAddon instance;
    @Setter private FoodItems foodItems = FoodItems.DEFAULT;
    @Setter private ItemHelper itemHelper = ItemHelper.DEFAULT;
    @Setter private InventoryHelper inventoryHelper = InventoryHelper.DEFAULT;
    @Setter private ComponentHelper componentHelper = ComponentHelper.DEFAULT;
    @Setter private TickManager tickManager = TickManager.DEFAULT;

    public TooltipAddon() {
        instance = this;
    }

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

    public static TickManager tickManager() {
        return instance.tickManager;
    }

    private static <T> void versionedReference(T reference, Consumer<T> setter) {
        if (reference != null) setter.accept(reference);
    }

    //TODO View Components separate and cycle through them (2.0)

    @Override
    protected void preConfigurationLoad() {
        Laby.references().revisionRegistry().register(new SimpleRevision("advancedtooltip", new SemanticVersion("1.5.0"), "2023-10-13"));
        Laby.references().revisionRegistry().register(new SimpleRevision("advancedtooltip", new SemanticVersion("1.6.0"), "2024-04-23"));
        Laby.references().revisionRegistry().register(new SimpleRevision("advancedtooltip", new SemanticVersion("1.7.0"), "2024-05-04"));
        Laby.references().revisionRegistry().register(new SimpleRevision("advancedtooltip", new SemanticVersion("1.8.0"), "2024-06-18"));
    }

    @Override
    protected void enable() {
        initialiseReferences();
        registerSettingCategory();
        registerListener();
    }

    private void initialiseReferences() {
        DefaultReferenceStorage referenceStorage = this.referenceStorageAccessor();
        versionedReference(referenceStorage.getFoodItems(), this::foodItems);
        versionedReference(referenceStorage.getItemHelper(), this::itemHelper);
        versionedReference(referenceStorage.getInventoryHelper(), this::inventoryHelper);
        versionedReference(referenceStorage.getComponentHelper(), this::componentHelper);
        versionedReference(referenceStorage.getTickManager(), this::tickManager);
    }

    private void registerListener() {
        this.registerListener(new ItemStackTooltipListener(this, this.foodItems, this.itemHelper, this.componentHelper, this.tickManager));
    }

    @Override
    protected Class<TooltipConfiguration> configurationClass() {
        return TooltipConfiguration.class;
    }

}
