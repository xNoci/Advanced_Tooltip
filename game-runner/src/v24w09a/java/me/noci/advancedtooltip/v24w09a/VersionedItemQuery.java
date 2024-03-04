package me.noci.advancedtooltip.v24w09a;

import me.noci.advancedtooltip.core.referenceable.ItemQuery;
import me.noci.advancedtooltip.core.utils.MapDecorationLocation;
import me.noci.advancedtooltip.v24w09a.util.ItemCast;
import me.noci.advancedtooltip.v24w09a.util.components.ComponentUtils;
import net.labymod.api.client.world.effect.PotionEffect;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.labymod.api.nbt.tags.NBTTagCompound;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.block.CommandBlock;

import javax.inject.Singleton;
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
        var is = ItemCast.toMinecraftItemStack(itemStack);
        SuspiciousStewEffects stewEffects = is.get(DataComponents.SUSPICIOUS_STEW_EFFECTS);
        if (stewEffects == null) return Optional.empty();
        var effects = stewEffects.effects()
                .stream()
                .map(entry -> (PotionEffect) new MobEffectInstance(entry.effect(), entry.duration()))
                .toList();
        return Optional.of(effects);
    }

    @Override
    public Optional<FoodProperties> getFoodProperties(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItem(itemStack);
        var foodProperties = item.getFoodProperties();
        if (foodProperties == null) return Optional.empty();
        return Optional.of(new FoodProperties(foodProperties.getNutrition(), foodProperties.getSaturationModifier()));
    }

    @Override
    public Optional<String> displayItemData(ItemStack itemStack, boolean withNbtArrayData, boolean expandComponents) {
        var is = ItemCast.toMinecraftItemStack(itemStack);
        if (is.getComponents().isEmpty()) return Optional.empty();
        return Optional.of(ComponentUtils.prettyPrint(is, withNbtArrayData, expandComponents));
    }

    @Override
    public boolean isCommandBlock(ItemStack itemStack) {
        return ItemCast.toMinecraftBlockItem(itemStack)
                .filter(blockItem -> blockItem.getBlock() instanceof CommandBlock)
                .isPresent();
    }

    @Override
    public Optional<Integer> getRepairCost(ItemStack itemStack) {
        var is = ItemCast.toMinecraftItemStack(itemStack);
        Integer repairCost = is.get(DataComponents.REPAIR_COST);
        return Optional.ofNullable(repairCost);
    }

    @Override
    public Optional<List<MapDecorationLocation>> getMapDecorationLocations(ItemStack itemStack) {
        var is = ItemCast.toMinecraftItemStack(itemStack);

        var decorations = is.get(DataComponents.MAP_DECORATIONS);
        if (decorations == null) return Optional.empty();

        var mapLocations = decorations.decorations()
                .values()
                .stream()
                .map(entry -> new MapDecorationLocation((byte) entry.type().id(), entry.x(), entry.z()))
                .toList();
        return Optional.of(mapLocations);
    }

    @Override
    public Optional<NBTTagCompound> getBlockEntityTag(ItemStack itemStack) {
        var is = ItemCast.toMinecraftItemStack(itemStack);
        CustomData blockData = is.get(DataComponents.BLOCK_ENTITY_DATA);
        if (blockData == null) return Optional.empty();
        return Optional.of((NBTTagCompound) blockData.getUnsafe());
    }


}
