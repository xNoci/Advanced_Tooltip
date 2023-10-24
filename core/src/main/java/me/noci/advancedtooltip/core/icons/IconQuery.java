package me.noci.advancedtooltip.core.icons;

import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import me.noci.advancedtooltip.core.config.AdvancedTooltipConfiguration;
import me.noci.advancedtooltip.core.config.SaturationType;
import me.noci.advancedtooltip.core.referenceable.ItemQuery;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.util.collection.Lists;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public record IconQuery(TooltipIcon full_icon, TooltipIcon half_icon, ValidItemInterface validItemInterface,
                        ShowFunction showFunction,
                        LevelFunction levelFunction) {

    private static final List<IconQuery> iconQueries = Lists.newArrayList();

    static {
        iconQueries.add(new IconQuery(TooltipIcon.FULL_FOOD, TooltipIcon.HALF_FOOD, (iq, is) -> is.isFood(), AdvancedTooltipConfiguration::showFoodLevel, (c, iq, is) -> iq.getNutrition(is)));
        iconQueries.add(new IconQuery(TooltipIcon.FULL_SATURATION, TooltipIcon.HALF_SATURATION, (iq, is) -> is.isFood(), AdvancedTooltipConfiguration::showSaturationLevel, (c, iq, is) -> (c.saturationType() == SaturationType.MAX_SATURATION) ? iq.getSaturationIncrement(is) : iq.getAddedSaturation(is)));
        iconQueries.add(new IconQuery(TooltipIcon.FULL_ARMOR, TooltipIcon.HALF_ARMOR, ItemQuery::isArmor, AdvancedTooltipConfiguration::showArmorBarIcons, (c, iq, is) -> iq.getArmorBars(is)));
    }

    public static <T extends ClientIconComponent> List<T> getIcons(ItemStack itemStack, Function<List<TooltipIcon>, T> convert) {
        AdvancedTooltipAddon addon = AdvancedTooltipAddon.getInstance();
        AdvancedTooltipConfiguration configuration = addon.configuration();
        if (configuration.developerSettings().showNBTData()) return List.of();

        List<T> icons = Lists.newArrayList();

        for (IconQuery iconQuery : iconQueries) {
            iconQuery.apply(icons, itemStack, convert);
        }

        if (icons.size() > 0) {
            icons.get(0).setFirstComponent();
            icons.get(icons.size() - 1).setLastComponent();
        }

        return icons;
    }

    private boolean shouldShow(AdvancedTooltipConfiguration configuration) {
        return showFunction.apply(configuration);
    }

    private float getLevel(AdvancedTooltipConfiguration configuration, ItemQuery itemQuery, ItemStack itemStack) {
        return levelFunction.apply(configuration, itemQuery, itemStack).map(Number::floatValue).orElse(0F);
    }

    private boolean isItemValid(ItemStack itemStack, ItemQuery itemQuery) {
        return validItemInterface.isValid(itemQuery, itemStack);
    }

    private <T> void apply(List<T> icons, ItemStack itemStack, Function<List<TooltipIcon>, T> convert) {
        AdvancedTooltipAddon addon = AdvancedTooltipAddon.getInstance();
        AdvancedTooltipConfiguration configuration = addon.configuration();
        ItemQuery itemQuery = addon.getItemQuery();

        if (!isItemValid(itemStack, itemQuery)) return;
        if (!shouldShow(configuration)) return;
        List<TooltipIcon> temp = Lists.newArrayList();

        float level = getLevel(configuration, itemQuery, itemStack);
        while (level >= 2) {
            level -= 2;
            temp.add(full_icon);
        }

        if (level > 0) {
            temp.add(half_icon);
        }

        if (temp.isEmpty()) return;
        icons.add(convert.apply(temp));
    }

    @FunctionalInterface
    private interface ValidItemInterface {
        boolean isValid(ItemQuery itemQuery, ItemStack itemStack);
    }

    @FunctionalInterface
    private interface LevelFunction {
        Optional<? extends Number> apply(AdvancedTooltipConfiguration configuration, ItemQuery itemQuery, ItemStack itemStack);
    }

    @FunctionalInterface
    private interface ShowFunction {
        boolean apply(AdvancedTooltipConfiguration configuration);
    }

}
