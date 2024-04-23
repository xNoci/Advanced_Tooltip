package me.noci.advancedtooltip.core.utils;

import java.util.Arrays;
import java.util.Objects;

public record SignText(String[] frontText, String[] backText, boolean hasFrontText, boolean hasBackText) {

    public SignText {
        hasFrontText = frontText != null && frontText.length > 0 && Arrays.stream(frontText).filter(Objects::nonNull).anyMatch(text -> !text.isBlank());
        hasBackText = backText != null && backText.length > 0 && Arrays.stream(backText).filter(Objects::nonNull).anyMatch(text -> !text.isBlank());
    }

    public SignText(String[] frontText, String[] backText) {
        this(frontText, backText, false, false);
    }

}
