package me.noci.advancedtooltip.core.utils;

import lombok.Getter;
import net.labymod.api.Laby;
import net.labymod.api.client.resources.ResourceLocation;

import java.util.function.Predicate;

public record MapDecoration(Type type, double x, double z) {

    public enum Type {
        PLAYER(0, "player"),
        FRAME(1, "frame"),
        RED_MARKER(2, "red_marker"),
        BLUE_MARKER(3, "blue_marker"),
        TARGET_X(4, "target_x"),
        TARGET_POINT(5, "target_point"),
        PLAYER_OFF_MAP(6, "player_off_map"),
        PLAYER_OFF_LIMITS(7, "player_off_limits"),
        MANSION(8, "mansion", true),
        MONUMENT(9, "monument", true),
        BANNER_WHITE(10, "banner_white"),
        BANNER_ORANGE(11, "banner_orange"),
        BANNER_MAGENTA(12, "banner_magenta"),
        BANNER_LIGHT_BLUE(13, "banner_light_blue"),
        BANNER_YELLOW(14, "banner_yellow"),
        BANNER_LIME(15, "banner_lime"),
        BANNER_PINK(16, "banner_pink"),
        BANNER_GRAY(17, "banner_gray"),
        BANNER_LIGHT_GRAY(18, "banner_light_gray"),
        BANNER_CYAN(19, "banner_cyan"),
        BANNER_PURPLE(20, "banner_purple"),
        BANNER_BLUE(21, "banner_blue"),
        BANNER_BROWN(22, "banner_brown"),
        BANNER_GREEN(23, "banner_green"),
        BANNER_RED(24, "banner_red"),
        BANNER_BLACK(25, "banner_black"),
        BURIED_TREASURE(26, "red_x", true),
        DESERT_VILLAGE(27, "village_desert", true),
        PLAINS_VILLAGE(28, "village_plains", true),
        SAVANNA_VILLAGE(29, "village_savanna", true),
        SNOWY_VILLAGE(30, "village_snowy", true),
        TAIGA_VILLAGE(31, "village_taiga", true),
        JUNGLE_EXPLORER(32, "jungle_temple", true),
        SWAMP_VILLAGE(33, "swamp_hut", true),
        TRIAL_CHAMBER("trial_chambers"),
        UNKNOWN("advancedtooltip_unknown_map_type");

        private final int id;
        private final ResourceLocation resourceLocation;
        @Getter private final boolean showInTooltip;
        @Getter private final String translationKey;

        Type(String resourceLocation) {
            this(-1, resourceLocation, true);
        }

        Type(int id, String resourceLocation) {
            this(id, resourceLocation, false);
        }

        Type(int id, String resourceLocation, boolean showInTooltip) {
            this.id = id;
            this.resourceLocation = Laby.references().resources().resourceLocationFactory().createMinecraft(resourceLocation);
            this.showInTooltip = showInTooltip;
            this.translationKey = "explorer_map." + name().toLowerCase();
        }

        public static Type byType(int type) {
            for (Type value : values()) {
                if (value.id == type) return value;
            }

            return UNKNOWN;
        }

        public static Type byResourceLocation(Predicate<ResourceLocation> resourceLocation) {
            for (Type value : values()) {
                if (resourceLocation.test(value.resourceLocation)) return value;
            }

            return UNKNOWN;
        }

    }

}
