package me.noci.advancedtooltip.core;

import lombok.Getter;
import me.noci.advancedtooltip.core.config.AdvancedTooltipConfiguration;
import me.noci.advancedtooltip.core.generated.DefaultReferenceStorage;
import me.noci.advancedtooltip.core.listener.FoodItemTooltipDebugListener;
import me.noci.advancedtooltip.core.utils.DefaultFoodInfo;
import me.noci.advancedtooltip.core.utils.FoodInfo;
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

    @Getter private FoodInfo foodInfo = new DefaultFoodInfo();

    public AdvancedTooltipAddon() {
        instance = this;
    }

    @Override
    protected void enable() {
        DefaultReferenceStorage referenceStorage = this.referenceStorageAccessor();
        FoodInfo foodInfo = referenceStorage.getFoodInfo();
        if (foodInfo != null) {
            this.foodInfo = foodInfo;
        }

        this.registerSettingCategory();

        if (Laby.labyAPI().labyModLoader().isAddonDevelopmentEnvironment()) {
            this.registerListener(new FoodItemTooltipDebugListener(this, foodInfo));
        }

        this.logger().info("Enabled the Addon");
    }

    @Override
    protected Class<AdvancedTooltipConfiguration> configurationClass() {
        return AdvancedTooltipConfiguration.class;
    }
}
