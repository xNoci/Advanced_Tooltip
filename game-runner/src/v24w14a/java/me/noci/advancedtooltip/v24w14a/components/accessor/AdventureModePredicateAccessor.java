package me.noci.advancedtooltip.v24w14a.components.accessor;

import net.minecraft.advancements.critereon.BlockPredicate;

import java.util.List;

public interface AdventureModePredicateAccessor {

    boolean showInTooltip();

    List<BlockPredicate> predicates();
}