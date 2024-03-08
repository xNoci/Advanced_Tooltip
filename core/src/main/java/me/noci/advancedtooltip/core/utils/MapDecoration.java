package me.noci.advancedtooltip.core.utils;

import lombok.Getter;

public record MapDecoration(Type type, double x, double z) {

    public enum Type {
        PLAYER(0),
        FRAME(1),
        RED_MARKER(2),
        BLUE_MARKER(3),
        TARGET_X(4),
        TARGET_POINT(5),
        PLAYER_OFF_MAP(6),
        PLAYER_OFF_LIMITS(7),
        MANSION(8, true),
        MONUMENT(9, true),
        BANNER_WHITE(10),
        BANNER_ORANGE(11),
        BANNER_MAGENTA(12),
        BANNER_LIGHT_BLUE(13),
        BANNER_YELLOW(14),
        BANNER_LIME(15),
        BANNER_PINK(16),
        BANNER_GRAY(17),
        BANNER_LIGHT_GRAY(18),
        BANNER_CYAN(19),
        BANNER_PURPLE(20),
        BANNER_BLUE(21),
        BANNER_BROWN(22),
        BANNER_GREEN(23),
        BANNER_RED(24),
        BANNER_BLACK(25),
        BURIED_TREASURE(26, true),
        DESERT_VILLAGE(27, true),
        PLAINS_VILLAGE(28, true),
        SAVANNA_VILLAGE(29, true),
        SNOWY_VILLAGE(30, true),
        TAIGA_VILLAGE(31, true),
        JUNGLE_EXPLORER(32, true),
        SWAMP_VILLAGE(33, true),
        UNKNOWN(-1, true);

        private final int id;
        @Getter private final boolean showInTooltip;
        @Getter private final String translationKey;

        Type(int id) {
            this.id = id;
            this.showInTooltip = false;
            this.translationKey = "explorer_map." + name().toLowerCase();
        }

        Type(int id, boolean showInTooltip) {
            this.id = id;
            this.showInTooltip = showInTooltip;
            this.translationKey = "explorer_map." + name().toLowerCase();
        }

        public static Type byType(int type) {
            for (Type value : values()) {
                if (value.id == type) return value;
            }
            return UNKNOWN;
        }

    }

}
