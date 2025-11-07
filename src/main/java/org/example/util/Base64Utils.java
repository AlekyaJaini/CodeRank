package org.example.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Utils {

    private Base64Utils() {
        // prevent instantiation
    }

    public static String encode(String text) {
        if (text == null) return "";
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    public static String decode(String base64Text) {
        if (base64Text == null || base64Text.isEmpty()) return "";
        return new String(Base64.getDecoder().decode(base64Text), StandardCharsets.UTF_8);
    }
}
