package me.noci.advancedtooltip.v24w11a.items;

import me.noci.advancedtooltip.core.referenceable.items.ItemHelper;
import me.noci.advancedtooltip.v24w11a.utils.ItemCast;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

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
            int efficiency = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.EFFICIENCY, ItemCast.toMinecraftItemStack(itemStack));
            int modifier = efficiency > 0 ? efficiency * efficiency + 1 : 0;
            speed = speed.map(speedValue -> speedValue + modifier);
        }

        return speed;
    }

    @Override
    public Optional<Integer> discSignalStrengt(ItemStack itemStack) {
        return ItemCast.asItem(itemStack, RecordItem.class).map(RecordItem::getAnalogOutput);
    }

}
