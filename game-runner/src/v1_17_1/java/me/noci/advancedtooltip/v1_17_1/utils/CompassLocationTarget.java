package me.noci.advancedtooltip.v1_16_5.utils;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record CompassLocationTarget(@Nullable ResourceKey<Level> dimension, int x, int y, int z) {

    private static final CompassLocationTarget EMPTY = new CompassLocationTarget(null, -1, -1, -1);

    @Nullable
    public static CompassLocationTarget from(Level level, ItemStack itemStack) {
        ResourceKey<Level> dimension;
        BlockPos location;

        if (CompassItem.isLodestoneCompass(itemStack)) {
            CompoundTag tag = itemStack.getTag();
            if (tag == null) return null;
            boolean hasLodestonePos = tag.contains("LodestonePos");
            boolean hasLodestoneDimension = tag.contains("LodestoneDimension");

            if (!hasLodestonePos || !hasLodestoneDimension) {
                return EMPTY;
            }

            Optional<ResourceKey<Level>> lodestoneDimension = CompassItem.getLodestoneDimension(tag);
            if (lodestoneDimension.isEmpty() || level.dimension() != lodestoneDimension.get()) {
                return EMPTY;
            }

            dimension = lodestoneDimension.get();
            location = NbtUtils.readBlockPos(tag.getCompound("LodestonePos"));
        } else {
            if (!level.dimensionType().natural()) return EMPTY;
            dimension = level.dimension();
            location = switch (level) {
                case ClientLevel clientLevel -> clientLevel.getSharedSpawnPos();
                case ServerLevel serverLevel -> serverLevel.getSharedSpawnPos();
                default -> null;
            };
        }

        if (location == null) return EMPTY;
        return new CompassLocationTarget(dimension, location.getX(), location.getY(), location.getZ());
    }

}
