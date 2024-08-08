package me.noci.advancedtooltip.v1_21_1.mixins.component.accessor;

import me.noci.advancedtooltip.v1_21_1.components.accessor.AdventureModePredicateAccessor;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.world.item.AdventureModePredicate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(AdventureModePredicate.class)
public class AdventureModePredicateMixin implements AdventureModePredicateAccessor {

    @Final @Shadow private boolean showInTooltip;

    @Final @Shadow private List<BlockPredicate> predicates;


    @Override
    public boolean showInTooltip() {
        return showInTooltip;
    }

    @Override
    public List<BlockPredicate> predicates() {
        return predicates;
    }
}
