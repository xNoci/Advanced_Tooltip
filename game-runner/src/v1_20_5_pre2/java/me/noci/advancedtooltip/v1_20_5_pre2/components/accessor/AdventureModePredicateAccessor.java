package me.noci.advancedtooltip.v1_20_5_pre2.components.accessor;

import net.minecraft.advancements.critereon.BlockPredicate;

import java.util.List;

public interface AdventureModePredicateAccessor {

    boolean showInTooltip();

    List<BlockPredicate> predicates();
}
