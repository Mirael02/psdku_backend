package com.portalpolinema.backend.shared;

public class SlugUtil {

    public static String slugifyTemp(String text) {
        String base = slugifyBase(text);
        return base + "_temp";
    }

    public static String slugifyFinal(String text) {
        return slugifyBase(text);
    }

    private static String slugifyBase(String text) {
        if (text == null)
            return "";
        String result = text.toLowerCase();
        result = result.replaceAll("[^a-z0-9]+", "-");
        return result.replaceAll("^-+|-+$", "");
    }

}