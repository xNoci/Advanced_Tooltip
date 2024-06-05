package me.noci.advancedtooltip.core.referenceable;

import net.labymod.api.reference.annotation.Referenceable;
import net.labymod.api.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public interface TickManager {

    TickManager DEFAULT = () -> 20f;

    float tickRate();

    default int seconds(int ticks, boolean useTickRate) {
        float tickRate = useTickRate ? tickRate() : 20;
        return MathHelper.floor(ticks / tickRate);
    }

    default int minutes(int ticks, boolean useTickRate) {
        float tickRate = useTickRate ? tickRate() : 20;
        return MathHelper.floor(ticks / (60 * tickRate));
    }

}
