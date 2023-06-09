package me.noci.advancedtooltip.core.utils;

import lombok.Getter;

public class MapLocation {

    @Getter private final MapType type;
    @Getter private final double x;
    @Getter private final double z;

    public MapLocation(byte type, double x, double z) {
        this.type = MapType.byType(type);
        this.x = x;
        this.z = z;
    }

    public enum MapType {

        WOODLAND((byte) 8),
        OCEAN((byte) 9),
        BURIED_TREASURE((byte) 26),
        UNKNOWN((byte) -1);

        private final byte id;

        MapType(byte id) {
            this.id = id;
        }

        public static MapType byType(byte type) {
            for (MapType value : values()) {
                if (value.id == type) return value;
            }
            return UNKNOWN;
        }

    }

}
