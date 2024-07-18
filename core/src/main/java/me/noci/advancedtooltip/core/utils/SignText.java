package me.noci.advancedtooltip.core.utils;

public record SignText(String[] frontText, String[] backText, boolean hasFrontText, boolean hasBackText) {

    public SignText {
        hasFrontText = frontText != null && frontText.length > 0 && hasWrittenString(frontText);
        hasBackText = backText != null && backText.length > 0 && hasWrittenString(backText);
    }

    public SignText(String[] frontText, String[] backText) {
        this(frontText, backText, false, false);
    }

    private boolean hasWrittenString(String[] array) {
        for (String s : array) {
            if (s != null && !s.isBlank()) return true;
        }
        return false;
    }

}
