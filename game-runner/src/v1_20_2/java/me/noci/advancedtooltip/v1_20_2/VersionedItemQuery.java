package me.noci.advancedtooltip.v1_20_2;

import me.noci.advancedtooltip.core.referenceable.ItemQuery;
import me.noci.advancedtooltip.core.utils.NBTUtils;
import me.noci.advancedtooltip.v1_20_2.util.ItemCast;
import net.labymod.api.client.world.effect.PotionEffect;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.labymod.api.nbt.NBTTagType;
import net.labymod.api.nbt.tags.NBTTagCompound;
import net.labymod.api.nbt.tags.NBTTagList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.CommandBlock;
import org.apache.commons.compress.utils.Lists;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
@Implements(ItemQuery.class)
public class VersionedItemQuery implements ItemQuery {

    @Override
    public boolean isArmor(ItemStack itemStack) {
        return ItemCast.toMinecraftItem(itemStack) instanceof ArmorItem;
    }

    @Override
    public Optional<Integer> getArmorBars(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItem(itemStack);
        if (!(item instanceof ArmorItem armorItem)) return Optional.empty();
        return Optional.of(armorItem.getDefense());
    }

    @Override
    public boolean isMapItem(ItemStack itemStack) {
        return ItemCast.toMinecraftItem(itemStack) instanceof MapItem;
    }

    @Override
    public Optional<Integer> getDiscSignalStrengt(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItem(itemStack);
        if (!(item instanceof RecordItem recordItem)) return Optional.empty();
        return Optional.of(recordItem.getAnalogOutput());
    }

    @Override
    public Optional<List<PotionEffect>> getStewEffect(ItemStack itemStack) {
        if (!itemStack.hasNBTTag() || !itemStack.getNBTTag().contains("effects")) return Optional.empty();
        Item item = ItemCast.toMinecraftItem(itemStack);
        if (!(item instanceof SuspiciousStewItem)) return Optional.empty();
        ArrayList<PotionEffect> stewEffects = Lists.newArrayList();

        NBTTagList<NBTTagCompound, ?> effects = itemStack.getNBTTag().getList("effects", NBTTagType.COMPOUND);
        NBTUtils.listIterator(effects).forEachRemaining(compound -> {
            int duration = SuspiciousStewItem.DEFAULT_DURATION;
            if (compound.contains("duration", NBTTagType.INT)) {
                duration = compound.getInt("duration");
            }


            MobEffect mobEffect = BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.tryParse(compound.getString("id")));
            stewEffects.add((PotionEffect) new MobEffectInstance(mobEffect, duration));
        });

        return Optional.of(stewEffects);
    }

    @Override
    public Optional<FoodProperties> getFoodProperties(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItem(itemStack);
        var foodProperties = item.getFoodProperties();
        if (foodProperties == null) return Optional.empty();
        return Optional.of(new FoodProperties(foodProperties.getNutrition(), foodProperties.getSaturationModifier()));
    }

    @Override
    public Optional<String> getItemNBTData(ItemStack itemStack, boolean withArrayContent) {
        if (!itemStack.hasNBTTag()) return Optional.empty();
        return Optional.of(NbtUtils.prettyPrint((Tag) itemStack.getNBTTag(), withArrayContent));
    }

    @Override
    public boolean isCommandBlock(ItemStack itemStack) {
        return ItemCast.toMinecraftBlockItem(itemStack)
                .filter(blockItem -> blockItem.getBlock() instanceof CommandBlock)
                .isPresent();
    }
}
