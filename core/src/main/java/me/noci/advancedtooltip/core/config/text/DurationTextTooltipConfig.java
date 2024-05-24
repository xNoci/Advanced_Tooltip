package me.noci.advancedtooltip.core.config.text;

import me.noci.advancedtooltip.core.config.DurationTimeUnit;

public interface DurationTextTooltipConfig extends TooltipConfig {

    DurationTimeUnit durationUnit();

    String configKey();

}
