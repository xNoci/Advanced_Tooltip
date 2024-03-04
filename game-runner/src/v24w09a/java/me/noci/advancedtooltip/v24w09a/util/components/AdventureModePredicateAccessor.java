package me.noci.advancedtooltip.v24w09a.util.components;

import net.minecraft.advancements.critereon.BlockPredicate;

import java.util.List;

public interface AdventureModePredicateAccessor {

    default boolean isEmpty() {
        return predicates().isEmpty();
    }

    boolean showInTooltip();

    List<BlockPredicate> predicates();
}
