package me.noci.advancedtooltip.v24w09a.mixins.component.accessor;

import me.noci.advancedtooltip.v24w09a.components.accessor.AttributeModifierAccessor;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AttributeModifier.class)
public class AttributeModifierMixin implements AttributeModifierAccessor {

    @Final
    @Shadow String name;

    @Override
    public String getName() {
        return this.name;
    }
}
