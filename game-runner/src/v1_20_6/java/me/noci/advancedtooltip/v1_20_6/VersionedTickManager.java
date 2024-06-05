package me.noci.advancedtooltip.v1_20_6;

import me.noci.advancedtooltip.core.referenceable.TickManager;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.level.Level;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
@Implements(TickManager.class)
public class VersionedTickManager implements TickManager {

    @Override
    public float tickRate() {
        return Optional.ofNullable(Minecraft.getInstance().level)
                .map(Level::tickRateManager)
                .map(TickRateManager::tickrate)
                .orElse(20f);
    }

}
