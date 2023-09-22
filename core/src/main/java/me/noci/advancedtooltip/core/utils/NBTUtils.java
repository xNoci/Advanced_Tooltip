package me.noci.advancedtooltip.core.utils;

import net.labymod.api.nbt.tags.NBTTagList;

import java.util.Iterator;
import java.util.stream.Stream;

public class NBTUtils {

    public static <T> Iterator<T> listIterator(NBTTagList<T, ?> list) {
        return list.tags().stream().map(tag -> (T) tag).iterator();
    }

    public static <T> Stream<T> asStream(NBTTagList<T, ?> list) {
        return (Stream<T>) list.tags().stream();
    }
}
