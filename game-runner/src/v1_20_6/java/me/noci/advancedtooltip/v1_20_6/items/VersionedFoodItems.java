package me.noci.advancedtooltip.v1_20_6.items;

import com.google.common.collect.Lists;
import me.noci.advancedtooltip.core.referenceable.items.FoodItems;
import me.noci.advancedtooltip.v1_20_6.utils.ItemCast;
import net.labymod.api.client.world.effect.PotionEffect;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import org.jetbrains.annotations.Nullable;

import javax.inject.Singleton;
import java.util.List;

@Singleton
@Implements(FoodItems.class)
public class VersionedFoodItems extends FoodItems.DefaultFoodItems {

    @Override
    public @Nullable List<PotionEffect> stewEffect(ItemStack itemStack) {
        SuspiciousStewEffects stewEffects = ItemCast.typedDataComponent(itemStack, DataComponents.SUSPICIOUS_STEW_EFFECTS);
        if (stewEffects == null) return null;

        List<PotionEffect> effects = Lists.newArrayList();

        for (SuspiciousStewEffects.Entry effect : stewEffects.effects()) {
            effects.add((PotionEffect) new MobEffectInstance(effect.effect(), effect.duration()));
        }

        return effects;
    }

    @Override
    public @Nullable FoodProperties foodProperties(ItemStack itemStack) {
        var properties = ItemCast.typedDataComponent(itemStack, DataComponents.FOOD);
        if (properties == null) return null;
        return new FoodProperties(properties.nutrition(), properties.saturation());
    }

}
