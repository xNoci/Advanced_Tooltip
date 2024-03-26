package me.noci.advancedtooltip.v24w12a.mixins.component.accessor;

import me.noci.advancedtooltip.v24w12a.components.accessor.ArmorTrimAccessor;
import net.minecraft.world.item.armortrim.ArmorTrim;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ArmorTrim.class)
public class ArmorTrimMixin implements ArmorTrimAccessor {

    @Final @Shadow boolean showInTooltip;

    @Override
    public boolean isShownInTooltip() {
        return showInTooltip;
    }

}
