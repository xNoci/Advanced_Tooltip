package me.noci.advancedtooltip.v1_19_3.items;

import me.noci.advancedtooltip.core.referenceable.items.ItemHelper;
import me.noci.advancedtooltip.core.utils.CompassTarget;
import me.noci.advancedtooltip.v1_19_3.utils.ItemCast;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.core.GlobalPos;
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
        return AbstractFurnaceBlockEntity.isFuel(ItemCast.toMinecraftItemStack(itemStack));
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
        RecordItem recordItem = ItemCast.asItem(itemStack, RecordItem.class);
        return recordItem != null ? recordItem.getLengthInTicks() : 0;
    }

    @Override
    public int burnDuration(ItemStack itemStack) {
        return AbstractFurnaceBlockEntity.getFuel().getOrDefault(ItemCast.toMinecraftItem(itemStack), 0);
    }

    @Override
    public @Nullable CompassTarget compassTarget(ItemStack labyItemStack) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        var item = itemStack.getItem();

        Player player = Minecraft.getInstance().player;
        if (player == null) return null;
        Level level = player.getLevel();

        GlobalPos pos = switch (item) {
            case Item i when i == Items.COMPASS ->
                    CompassItem.isLodestoneCompass(itemStack) ? CompassItem.getLodestonePosition(itemStack.getOrCreateTag()) : CompassItem.getSpawnPosition(level);
            case Item i when i == Items.RECOVERY_COMPASS -> player.getLastDeathLocation().orElse(null);
            default -> null;
        };

        if (pos == null) return null;
        boolean correctDimension = level.dimension().location().equals(pos.dimension().location());
        return new CompassTarget(correctDimension, pos.pos().getX(), pos.pos().getY(), pos.pos().getZ());
    }


}
