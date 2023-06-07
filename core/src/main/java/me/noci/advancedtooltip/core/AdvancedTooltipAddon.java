package me.noci.advancedtooltip.core;

import lombok.Getter;
import me.noci.advancedtooltip.core.config.AdvancedTooltipConfiguration;
import me.noci.advancedtooltip.core.generated.DefaultReferenceStorage;
import me.noci.advancedtooltip.core.listener.FoodItemTooltipDebugListener;
import me.noci.advancedtooltip.core.listener.ItemAnvilUsesListener;
import me.noci.advancedtooltip.core.utils.DefaultItemQuery;
import me.noci.advancedtooltip.core.utils.ItemQuery;
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

    public AdvancedTooltipAddon() {
        instance = this;
    }

    @Override
    protected void enable() {
        DefaultReferenceStorage referenceStorage = this.referenceStorageAccessor();
        ItemQuery itemQuery = referenceStorage.getItemQuery();
        if (itemQuery != null) {
            this.itemQuery = itemQuery;
        }

        this.registerSettingCategory();

        if (Laby.labyAPI().labyModLoader().isAddonDevelopmentEnvironment()) {
            this.registerListener(new FoodItemTooltipDebugListener(this, itemQuery));
        }

        this.registerListener(new ItemAnvilUsesListener(this, itemQuery));
    }

    @Override
    protected Class<AdvancedTooltipConfiguration> configurationClass() {
        return AdvancedTooltipConfiguration.class;
    }
}
