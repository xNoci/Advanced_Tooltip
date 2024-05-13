package me.noci.advancedtooltip.v24w19b.components.accessor;

import net.minecraft.advancements.critereon.BlockPredicate;

import java.util.List;

public interface AdventureModePredicateAccessor {

    boolean showInTooltip();

    List<BlockPredicate> predicates();
}
