package me.noci.advancedtooltip.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.labymod.api.nbt.NBTTag;
import net.labymod.api.nbt.NBTTagType;
import net.labymod.api.nbt.tags.NBTTagCompound;
import net.labymod.api.nbt.tags.NBTTagList;
import net.labymod.api.nbt.tags.NBTTagString;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public record SignText(String[] frontText, String[] backText) {
    private static final Gson GSON = new GsonBuilder().create();

    public boolean hasFrontText() {
        if (frontText == null || frontText.length == 0) return false;
        return Arrays.stream(frontText).filter(Objects::nonNull).anyMatch(text -> !text.isBlank());
    }

    public boolean hasBackText() {
        if (backText == null || backText.length == 0) return false;
        return Arrays.stream(backText).filter(Objects::nonNull).anyMatch(text -> !text.isBlank());
    }

    public static Optional<SignText> parseBelowOrEquals112(NBTTagCompound blockEntityTag) {
        String[] frontText = new String[4];

        for (int i = 1; i < 5; i++) {
            String tag = "Text" + i;
            if (blockEntityTag.contains(tag)) {
                frontText[i - 1] = blockEntityTag.getString(tag);
            }
        }

        return Optional.of(new SignText(frontText, null));
    }

    public static Optional<SignText> parseBelow120(NBTTagCompound blockEntityTag) {
        String[] frontText = new String[4];

        for (int i = 1; i < 5; i++) {
            String tag = "Text" + i;
            if (blockEntityTag.contains(tag)) {
                frontText[i - 1] = GSON.fromJson(blockEntityTag.getString(tag), JsonObject.class).get("text").getAsString();
            }
        }

        return Optional.of(new SignText(frontText, null));
    }


    public static Optional<SignText> parseAboveOrEquals120(NBTTagCompound blockEntityTag) {
        String[] frontText = null;
        String[] backText = null;

        if (blockEntityTag.contains("front_text")) {
            NBTTagCompound frontTextCompound = blockEntityTag.getCompound("front_text");
            NBTTagList<NBTTagString, ?> lines = frontTextCompound.getList("messages", NBTTagType.STRING);
            frontText = NBTUtils.asStream(lines)
                    .map(NBTTag::value)
                    .map(line -> GSON.fromJson(line, JsonObject.class))
                    .map(jsonElement -> jsonElement.get("text").getAsString())
                    .toArray(String[]::new);
        }

        if (blockEntityTag.contains("back_text")) {
            NBTTagCompound frontTextCompound = blockEntityTag.getCompound("back_text");
            NBTTagList<NBTTagString, ?> lines = frontTextCompound.getList("messages", NBTTagType.STRING);
            backText = NBTUtils.asStream(lines)
                    .map(NBTTag::value)
                    .map(line -> GSON.fromJson(line, JsonObject.class))
                    .map(jsonElement -> jsonElement.get("text").getAsString())
                    .toArray(String[]::new);
        }


        return Optional.of(new SignText(frontText, backText));
    }

}
