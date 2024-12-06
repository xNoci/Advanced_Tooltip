package me.noci.advancedtooltip.v1_21_4;

import me.noci.advancedtooltip.core.referenceable.TickManager;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

import javax.inject.Singleton;

@Singleton
@Implements(TickManager.class)
public class VersionedTickManager implements TickManager {

    @Override
    public float tickRate() {
        ClientLevel level = Minecraft.getInstance().level;
        return level == null ? 20 : level.tickRateManager().tickrate();
    }

}
