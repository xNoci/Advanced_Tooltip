package me.noci.advancedtooltip.core.utils;

import lombok.Getter;

public class MapDecorationLocation {

    private final MapDecoration type;
    @Getter private final double x;
    @Getter private final double z;

    public MapDecorationLocation(int type, double x, double z) {
        this.type = MapDecoration.byType(type);
        this.x = x;
        this.z = z;
    }

    public String typeAsString() {
        return type.name().toLowerCase();
    }

    public boolean showInToolTip() {
        return type.display;
    }

    public enum MapDecoration {

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
        private final boolean display;

        MapDecoration(int id) {
            this.id = id;
            this.display = false;
        }

        MapDecoration(int id, boolean display) {
            this.id = id;
            this.display = display;
        }

        public static MapDecoration byType(int type) {
            for (MapDecoration value : values()) {
                if (value.id == type) return value;
            }
            return UNKNOWN;
        }

    }

}
