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

import javax.inject.Singleton;
import java.util.Optional;

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
    public Optional<Integer> armorBars(ItemStack itemStack) {
        return ItemCast.asItem(itemStack, ArmorItem.class).map(ArmorItem::getDefense);
    }

    @Override
    public Optional<Integer> miningLevel(ItemStack itemStack) {
        return ItemCast.asItem(itemStack, TieredItem.class).map(TieredItem::getTier).map(Tier::getLevel);
    }

    @Override
    public Optional<Float> miningSpeed(ItemStack itemStack, boolean applyEnchantments) {
        var speed = ItemCast.asItem(itemStack, TieredItem.class).map(TieredItem::getTier).map(Tier::getSpeed);

        if (applyEnchantments) {
            int efficiency = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, ItemCast.toMinecraftItemStack(itemStack));
            int modifier = efficiency > 0 ? efficiency * efficiency + 1 : 0;
            speed = speed.map(speedValue -> speedValue + modifier);
        }

        return speed;
    }

    @Override
    public Optional<Integer> discSignalStrengt(ItemStack itemStack) {
        return ItemCast.asItem(itemStack, RecordItem.class).map(RecordItem::getAnalogOutput);
    }

    @Override
    public Optional<Integer> discTickLength(ItemStack itemStack) {
        return ItemCast.asItem(itemStack, RecordItem.class).map(RecordItem::getLengthInTicks);
    }

    @Override
    public int burnDuration(ItemStack itemStack) {
        return AbstractFurnaceBlockEntity.getFuel().getOrDefault(ItemCast.toMinecraftItem(itemStack), 0);
    }

    @Override
    public Optional<CompassTarget> compassTarget(ItemStack labyItemStack) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        var item = itemStack.getItem();

        Player player = Minecraft.getInstance().player;
        if (player == null) return Optional.empty();
        Level level = player.getLevel();

        GlobalPos pos = switch (item) {
            case Item i when i == Items.COMPASS ->
                    CompassItem.isLodestoneCompass(itemStack) ? CompassItem.getLodestonePosition(itemStack.getOrCreateTag()) : CompassItem.getSpawnPosition(level);
            case Item i when i == Items.RECOVERY_COMPASS -> player.getLastDeathLocation().orElse(null);
            default -> null;
        };

        if (pos == null) return Optional.empty();
        boolean correctDimension = level.dimension().location().equals(pos.dimension().location());
        CompassTarget target = new CompassTarget(correctDimension, pos.pos().getX(), pos.pos().getY(), pos.pos().getZ());
        return Optional.of(target);
    }


}
