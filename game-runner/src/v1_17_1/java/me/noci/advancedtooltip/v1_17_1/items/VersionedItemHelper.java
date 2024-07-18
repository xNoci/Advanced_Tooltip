package me.noci.advancedtooltip.v1_17_1.items;

import me.noci.advancedtooltip.core.referenceable.items.ItemHelper;
import me.noci.advancedtooltip.core.utils.CompassTarget;
import me.noci.advancedtooltip.v1_17_1.utils.CompassLocationTarget;
import me.noci.advancedtooltip.v1_17_1.utils.ItemCast;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.jetbrains.annotations.Nullable;

import javax.inject.Singleton;

@Singleton
@Implements(ItemHelper.class)
public class VersionedItemHelper implements ItemHelper {

    @Override
    public boolean isArmor(ItemStack itemStack) {
        return ItemCast.toMinecraftItem(itemStack) instanceof ArmorItem;
    }

    @Override
    public boolean isMiningTool(ItemStack itemStack) {
        return ItemCast.toMinecraftItem(itemStack) instanceof DiggerItem;
    }

    @Override
    public boolean isClock(ItemStack itemStack) {
        return ItemCast.toMinecraftItem(itemStack) == Items.CLOCK;
    }

    @Override
    public boolean isFuel(ItemStack itemStack) {
        try {
            return AbstractFurnaceBlockEntity.isFuel(ItemCast.toMinecraftItemStack(itemStack));
        } catch (IllegalStateException e) {
            return false;
        }
    }

    @Override
    public int armorBars(ItemStack itemStack) {
        ArmorItem armor = ItemCast.asItem(itemStack, ArmorItem.class);
        return armor != null ? armor.getDefense() : 0;
    }

    @Override
    public int miningLevel(ItemStack itemStack) {
        TieredItem tieredItem = ItemCast.asItem(itemStack, TieredItem.class);
        return tieredItem != null ? tieredItem.getTier().getLevel() : 0;
    }

    @Override
    public float miningSpeed(ItemStack itemStack, boolean applyEnchantments) {
        TieredItem tieredItem = ItemCast.asItem(itemStack, TieredItem.class);
        if (tieredItem == null) return 0;
        float speed = tieredItem.getTier().getSpeed();

        if (applyEnchantments) {
            int efficiency = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, ItemCast.toMinecraftItemStack(itemStack));
            int modifier = efficiency > 0 ? efficiency * efficiency + 1 : 0;
            speed += modifier;
        }

        return speed;
    }

    @Override
    public int discSignalStrengt(ItemStack itemStack) {
        RecordItem recordItem = ItemCast.asItem(itemStack, RecordItem.class);
        return recordItem != null ? recordItem.getAnalogOutput() : 0;
    }

    @Override
    public int discTickLength(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int burnDuration(ItemStack itemStack) {
        return AbstractFurnaceBlockEntity.getFuel().getOrDefault(ItemCast.toMinecraftItem(itemStack), 0);
    }

    @Override
    public @Nullable CompassTarget compassTarget(ItemStack labyItemStack) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        var item = itemStack.getItem();

        if (item != Items.COMPASS) return null;

        Player player = Minecraft.getInstance().player;
        if (player == null) return null;
        Level level = player.level;

        CompassLocationTarget targetLocation = CompassLocationTarget.from(level, itemStack);

        if (targetLocation == null) return null;
        ResourceLocation dimensionLocation = targetLocation.dimension() != null ? targetLocation.dimension().location() : null;
        boolean correctDimension = level.dimension().location().equals(dimensionLocation);
        return new CompassTarget(correctDimension, targetLocation.x(), targetLocation.y(), targetLocation.z());
    }

}
