package me.noci.advancedtooltip.v1_8_9.items;

import me.noci.advancedtooltip.core.referenceable.items.ItemHelper;
import me.noci.advancedtooltip.v1_8_9.items.accessors.ItemToolAccessor;
import me.noci.advancedtooltip.v1_8_9.utils.ItemCast;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemTool;
import net.minecraft.tileentity.TileEntityFurnace;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
@Implements(ItemHelper.class)
public class VersionedItemHelper implements ItemHelper {

    private static final int ITEM_RECORD_13_ID = Item.getIdFromItem(Items.record_13);

    @Override
    public boolean isArmor(ItemStack itemStack) {
        return ItemCast.toMinecraftItem(itemStack) instanceof ItemArmor;
    }

    @Override
    public boolean isMiningTool(ItemStack itemStack) {
        return ItemCast.toMinecraftItem(itemStack) instanceof ItemTool;
    }

    @Override
    public boolean isClock(ItemStack itemStack) {
        return ItemCast.toMinecraftItem(itemStack) == Items.clock;
    }

    @Override
    public boolean isFuel(ItemStack itemStack) {
        var mcItemStack = ItemCast.toMinecraftItemStack(itemStack);
        return TileEntityFurnace.isItemFuel(mcItemStack) || mcItemStack.getItem() == Items.lava_bucket;
    }

    @Override
    public Optional<Integer> armorBars(ItemStack itemStack) {
        return ItemCast.asItem(itemStack, ItemArmor.class).map(itemArmor -> itemArmor.getArmorMaterial().getDamageReductionAmount(itemArmor.armorType));
    }

    @Override
    public Optional<Integer> miningLevel(ItemStack itemStack) {
        return ItemCast.asItem(itemStack, ItemTool.class)
                .map(itemTool -> ((ItemToolAccessor) itemTool).toolMaterial())
                .map(Item.ToolMaterial::getHarvestLevel);
    }

    @Override
    public Optional<Float> miningSpeed(ItemStack itemStack, boolean applyEnchantments) {
        var speed = ItemCast.asItem(itemStack, ItemTool.class)
                .map(itemTool -> ((ItemToolAccessor) itemTool).toolMaterial())
                .map(Item.ToolMaterial::getEfficiencyOnProperMaterial);

        if (applyEnchantments) {
            int efficiency = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, ItemCast.toMinecraftItemStack(itemStack));
            int modifier = efficiency > 0 ? efficiency * efficiency + 1 : 0;
            speed = speed.map(speedValue -> speedValue + modifier);
        }

        return speed;
    }

    @Override
    public Optional<Integer> discSignalStrengt(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItem(itemStack);
        if (!(item instanceof ItemRecord)) return Optional.empty();
        return Optional.of(Item.getIdFromItem(item) + 1 - ITEM_RECORD_13_ID);
    }

    @Override
    public int burnDuration(ItemStack itemStack) {
        return TileEntityFurnace.getItemBurnTime(ItemCast.toMinecraftItemStack(itemStack));
    }
}
