package me.noci.advancedtooltip.core.icons;

import java.util.List;

@FunctionalInterface
public interface VersionedIconComponentMapper<T extends IconComponent> {
    T apply(List<TooltipIcon> icons);
}
