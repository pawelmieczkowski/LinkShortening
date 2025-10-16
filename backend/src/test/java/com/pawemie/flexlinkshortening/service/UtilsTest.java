package com.pawemie.flexlinkshortening.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilsTest {

    @Test
    void shouldValidateValidUrls() {
        assertTrue(Utils.isValidUrl("https://example.com"));
        assertTrue(Utils.isValidUrl("http://example.org/path"));
        assertTrue(Utils.isValidUrl("ftp://server.net"));
        assertTrue(Utils.isValidUrl("example.com"));
    }

    @Test
    void shouldRejectInvalidUrls() {
        assertFalse(Utils.isValidUrl(null));
        assertFalse(Utils.isValidUrl(""));
        assertFalse(Utils.isValidUrl(" "));
        assertFalse(Utils.isValidUrl("notaurl"));
        assertFalse(Utils.isValidUrl("http:/broken"));
        assertFalse(Utils.isValidUrl("://missing.scheme.com"));
    }
}