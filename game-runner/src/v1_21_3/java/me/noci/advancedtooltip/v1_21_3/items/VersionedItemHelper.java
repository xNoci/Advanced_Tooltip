package me.noci.advancedtooltip.v1_21_3.items;

import me.noci.advancedtooltip.core.referenceable.items.ItemHelper;
import me.noci.advancedtooltip.core.utils.CompassTarget;
import me.noci.advancedtooltip.v1_21_3.utils.ItemCast;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
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
        var level = Minecraft.getInstance().level;
        if (level == null) return false;
        return level.fuelValues().isFuel(ItemCast.toMinecraftItemStack(itemStack));
    }

    @Override
    public int armorBars(ItemStack itemStack) {
        ItemAttributeModifiers modifiers = ItemCast.typedDataComponent(itemStack, DataComponents.ATTRIBUTE_MODIFIERS);
        if (modifiers == null) return 0;
        for (ItemAttributeModifiers.Entry modifier : modifiers.modifiers()) {
            if (modifier.attribute().is(Attributes.ARMOR)) {
                return (int) modifier.modifier().amount();
            }
        }
        return 0;
    }

    @Override
    public int miningLevel(ItemStack itemStack) {
        return 0;
    }

    @Override
    public float miningSpeed(ItemStack labyItemStack, boolean applyEnchantments) {
        Tool tool = ItemCast.typedDataComponent(labyItemStack, DataComponents.TOOL);
        if (tool == null) return 0;
        float speed = tool.defaultMiningSpeed();

        for (Tool.Rule rule : tool.rules()) {
            if (rule.correctForDrops().orElse(false) && rule.speed().isPresent()) {
                speed = rule.speed().get();
                break;
            }
        }

        var level = Minecraft.getInstance().level;
        if (applyEnchantments && level != null) {
            var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
            var enchantmentHolder = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.EFFICIENCY);
            int efficiency = itemStack.getEnchantments().getLevel(enchantmentHolder);
            int modifier = efficiency > 0 ? efficiency * efficiency + 1 : 0;
            speed += modifier;
        }

        return speed;
    }

    @Override
    public int discSignalStrengt(ItemStack itemStack) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return 0;

        var holder = JukeboxSong.fromStack(player.level().registryAccess(), ItemCast.toMinecraftItemStack(itemStack)).orElse(null);
        if (holder == null) return 0;
        return holder.value().comparatorOutput();
    }

    @Override
    public int discTickLength(ItemStack itemStack) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return 0;

        var holder = JukeboxSong.fromStack(player.level().registryAccess(), ItemCast.toMinecraftItemStack(itemStack)).orElse(null);
        if (holder == null) return 0;
        return holder.value().lengthInTicks();
    }

    @Override
    public int burnDuration(ItemStack itemStack) {
        var level = Minecraft.getInstance().level;
        if (level == null) return 0;
        return level.fuelValues().burnDuration(ItemCast.toMinecraftItemStack(itemStack));
    }

    @Override
    public @Nullable CompassTarget compassTarget(ItemStack labyItemStack) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        var item = itemStack.getItem();

        Player player = Minecraft.getInstance().player;
        if (player == null) return null;
        Level level = player.level();

        GlobalPos pos = switch (item) {
            case Item i when i == Items.COMPASS -> {
                LodestoneTracker tracker = itemStack.get(DataComponents.LODESTONE_TRACKER);
                yield tracker != null ? tracker.target().orElse(null) : CompassItem.getSpawnPosition(level);
            }
            case Item i when i == Items.RECOVERY_COMPASS -> player.getLastDeathLocation().orElse(null);
            default -> null;
        };

        if (pos == null) return null;
        boolean correctDimension = level.dimension().location().equals(pos.dimension().location());
        return new CompassTarget(correctDimension, pos.pos().getX(), pos.pos().getY(), pos.pos().getZ());
    }
}
