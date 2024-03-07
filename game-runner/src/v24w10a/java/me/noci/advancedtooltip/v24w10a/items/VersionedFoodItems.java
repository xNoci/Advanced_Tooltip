package me.noci.advancedtooltip.v24w10a.items;

import me.noci.advancedtooltip.core.referenceable.items.FoodItems;
import me.noci.advancedtooltip.v24w10a.utils.ItemCast;
import net.labymod.api.client.world.effect.PotionEffect;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.SuspiciousStewEffects;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
@Implements(FoodItems.class)
public class VersionedFoodItems extends FoodItems.DefaultFoodItems {

    @Override
    public Optional<List<PotionEffect>> stewEffect(ItemStack labyItemStack) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        SuspiciousStewEffects stewEffects = itemStack.get(DataComponents.SUSPICIOUS_STEW_EFFECTS);
        if (stewEffects == null) return Optional.empty();
        var effects = stewEffects.effects()
                .stream()
                .map(entry -> (PotionEffect) new MobEffectInstance(entry.effect(), entry.duration()))
                .toList();
        return Optional.of(effects);
    }

    @Override
    public Optional<FoodProperties> foodProperties(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItem(itemStack);
        var foodProperties = item.getFoodProperties();
        if (foodProperties == null) return Optional.empty();
        return Optional.of(new FoodProperties(foodProperties.getNutrition(), foodProperties.getSaturationModifier()));
    }

}