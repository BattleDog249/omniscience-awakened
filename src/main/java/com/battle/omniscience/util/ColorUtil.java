package com.battle.omniscience.util;

/**
 * Helpers for manipulating packed ARGB color ints.
 */
public final class ColorUtil {

    private ColorUtil() {
    }

    /**
     * Replaces the alpha channel of a packed ARGB color, clamping the alpha to 0-255.
     *
     * @param argb  the packed ARGB color
     * @param alpha the new alpha value (0-255, clamped if out of range)
     * @return the color with the replaced alpha channel
     */
    public static int withAlpha(int argb, int alpha) {
        alpha = Math.max(0, Math.min(255, alpha));
        int red = (argb >> 16) & 0xFF;
        int green = (argb >> 8) & 0xFF;
        int blue = argb & 0xFF;
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }
}
