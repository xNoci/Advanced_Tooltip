package me.noci.advancedtooltip.v1_19_4;

import com.mojang.datafixers.util.Either;
import me.noci.advancedtooltip.core.utils.FoodInfo;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
@Implements(FoodInfo.class)
public class VersionedFoodInfo implements FoodInfo {

    @Override
    public int getFoodLevel(ItemStack itemStack) {
        FoodProperties foodProperties = getFoodProperties(itemStack);
        return foodProperties != null ? foodProperties.getNutrition() : -1;
    }

    @Override
    public float getSaturationModifier(ItemStack itemStack) {
        FoodProperties foodProperties = getFoodProperties(itemStack);
        return foodProperties != null ? foodProperties.getSaturationModifier() : -1;
    }

    @Nullable
    private Item parseItem(ResourceLocation location) {
        Optional<? extends Holder<Item>> itemLookupRegistry = BuiltInRegistries.ITEM.asLookup()
                .get(ResourceKey.create(Registries.ITEM, location));
        Holder<Item> itemHolder = Either.left(itemLookupRegistry.orElse(null)).left().orElse(null);
        if (itemHolder == null) {
            return null;
        }
        return itemHolder.value();
    }

    @Nullable
    private FoodProperties getFoodProperties(ItemStack itemStack) {
        ResourceLocation location = itemStack.getAsItem().getIdentifier().getMinecraftLocation();
        Item item = parseItem(location);
        if (item == null) {
            return null;
        }
        return item.getFoodProperties();
    }

}
