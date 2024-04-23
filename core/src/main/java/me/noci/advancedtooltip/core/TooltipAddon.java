package me.noci.advancedtooltip.core;

import lombok.Setter;
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

import java.util.function.Consumer;

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

    @Setter private FoodItems foodItems = FoodItems.DEFAULT;
    @Setter private ItemHelper itemHelper = ItemHelper.DEFAULT;
    @Setter private InventoryHelper inventoryHelper = InventoryHelper.DEFAULT;
    @Setter private ComponentHelper componentHelper = ComponentHelper.DEFAULT;

    public TooltipAddon() {
        instance = this;
    }

    @Override
    protected void preConfigurationLoad() {
        Laby.references().revisionRegistry().register(new SimpleRevision("advancedtooltip", new SemanticVersion("1.5.0"), "2023-10-13"));
        Laby.references().revisionRegistry().register(new SimpleRevision("advancedtooltip", new SemanticVersion("1.6.0"), "2024-04-23"));
    }

    @Override
    protected void enable() {
        initialiseReferences();
        registerSettingCategory();
        registerListener();
    }

    private void initialiseReferences() {
        DefaultReferenceStorage referenceStorage = this.referenceStorageAccessor();
        setVersionedReference(referenceStorage.getFoodItems(), this::foodItems);
        setVersionedReference(referenceStorage.getItemHelper(), this::itemHelper);
        setVersionedReference(referenceStorage.getInventoryHelper(), this::inventoryHelper);
        setVersionedReference(referenceStorage.getComponentHelper(), this::componentHelper);
    }

    private void registerListener() {
        this.registerListener(new ItemStackTooltipListener(this, this.foodItems, this.itemHelper, this.componentHelper));
    }

    @Override
    protected Class<TooltipConfiguration> configurationClass() {
        return TooltipConfiguration.class;
    }

    private static <T> void setVersionedReference(T reference, Consumer<T> setter) {
        if (reference == null) return;
        setter.accept(reference);
    }

}
