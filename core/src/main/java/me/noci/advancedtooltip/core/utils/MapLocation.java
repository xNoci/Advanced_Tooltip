package me.noci.advancedtooltip.core.utils;

import lombok.Getter;

public class MapLocation {

    private final MapType type;
    @Getter private final double x;
    @Getter private final double z;

    public MapLocation(byte type, double x, double z) {
        this.type = MapType.byType(type);
        this.x = x;
        this.z = z;
    }

    public String typeAsString() {
        return type.name().toLowerCase();
    }

    public enum MapType {

        WOODLAND((byte) 8),
        OCEAN((byte) 9),
        BURIED_TREASURE((byte) 26),
        DESERT_VILLAGE((byte) 27),
        PLAINS_VILLAGE((byte) 28),
        SAVANNA_VILLAGE((byte) 29),
        SNOWY_VILLAGE((byte) 30),
        TAIGA_VILLAGE((byte) 31),
        JUNGLE_EXPLORER((byte) 32),
        SWAMP_VILLAGE((byte) 33),
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
