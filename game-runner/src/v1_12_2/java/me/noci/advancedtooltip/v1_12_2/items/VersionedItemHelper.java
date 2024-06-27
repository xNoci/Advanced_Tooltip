package me.noci.advancedtooltip.v1_12_2.items;

import me.noci.advancedtooltip.core.referenceable.items.ItemHelper;
import me.noci.advancedtooltip.core.utils.CompassTarget;
import me.noci.advancedtooltip.v1_12_2.items.accessors.ItemToolAccessor;
import me.noci.advancedtooltip.v1_12_2.utils.ItemCast;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemTool;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import javax.inject.Singleton;

@Singleton
@Implements(ItemHelper.class)
public class VersionedItemHelper implements ItemHelper {

    private static final int ITEM_RECORD_13_ID = Item.getIdFromItem(Items.RECORD_13);

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
        return ItemCast.toMinecraftItem(itemStack) == Items.CLOCK;
    }

    @Override
    public boolean isFuel(ItemStack itemStack) {
        var mcItemStack = ItemCast.toMinecraftItemStack(itemStack);
        return TileEntityFurnace.isItemFuel(mcItemStack) || mcItemStack.getItem() == Items.LAVA_BUCKET;
    }

    @Override
    public int armorBars(ItemStack itemStack) {
        ItemArmor armor = ItemCast.asItem(itemStack, ItemArmor.class);
        if (armor == null) return 0;
        return armor.getArmorMaterial().getDamageReductionAmount(armor.getEquipmentSlot());
    }

    @Override
    public int miningLevel(ItemStack itemStack) {
        ItemTool tool = ItemCast.asItem(itemStack, ItemTool.class);
        if (tool == null) return 0;
        return ((ItemToolAccessor) tool).toolMaterial().getHarvestLevel();
    }

    @Override
    public float miningSpeed(ItemStack itemStack, boolean applyEnchantments) {
        ItemTool tool = ItemCast.asItem(itemStack, ItemTool.class);
        if (tool == null) return 0;

        float speed = ((ItemToolAccessor) tool).toolMaterial().getEfficiency();

        if (applyEnchantments) {
            int efficiency = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, ItemCast.toMinecraftItemStack(itemStack));
            int modifier = efficiency > 0 ? efficiency * efficiency + 1 : 0;
            speed += modifier;
        }

        return speed;
    }

    @Override
    public int discSignalStrengt(ItemStack itemStack) {
        Item item = ItemCast.toMinecraftItem(itemStack);
        if (!(item instanceof ItemRecord)) return 0;
        return Item.getIdFromItem(item) + 1 - ITEM_RECORD_13_ID;
    }

    @Override
    public int discTickLength(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int burnDuration(ItemStack itemStack) {
        return TileEntityFurnace.getItemBurnTime(ItemCast.toMinecraftItemStack(itemStack));
    }

    @Override
    public @Nullable CompassTarget compassTarget(ItemStack labyItemStack) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        var item = itemStack.getItem();

        if (item != Items.COMPASS) return null;

        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player == null) return null;
        World world = player.world;

        BlockPos targetLocation = world.getSpawnPoint();
        if (targetLocation == null) return null;
        return new CompassTarget(world.provider.isSurfaceWorld(), targetLocation.getX(), targetLocation.getY(), targetLocation.getZ());
    }

}
