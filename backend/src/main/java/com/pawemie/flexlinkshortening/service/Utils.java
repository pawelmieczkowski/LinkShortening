package com.pawemie.flexlinkshortening.service;

import org.apache.commons.validator.routines.UrlValidator;

import java.util.regex.Pattern;

public final class Utils {

    private Utils() {
    }

    public static boolean isValidUrl(String url) {
        if (url == null || url.isBlank()) {
            return false;
        }

        String[] schemes = {"http", "https", "ftp"};

        UrlValidator validator = new UrlValidator(schemes);

        return validator.isValid(url) || validator.isValid("http://" + url);
    }

    public static boolean isValidCode(String code) {
        final Pattern SAFE_CODE_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+$");

        return code != null && !code.isBlank() && SAFE_CODE_PATTERN.matcher(code).matches();
    }
}
