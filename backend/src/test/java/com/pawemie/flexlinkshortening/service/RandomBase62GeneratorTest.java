package com.pawemie.flexlinkshortening.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomBase62GeneratorTest {

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    @Test
    void shouldGenerateValidLengthCode() {
        String code = RandomBase62Generator.generateCode();
        assertNotNull(code);
        assertEquals(7, code.length());
    }

    @Test
    void shouldContainOnlyAllowedCharacters() {
        String code = RandomBase62Generator.generateCode();
        for (char c : code.toCharArray()) {
            assertTrue(ALPHABET.indexOf(c) >= 0,
                    () -> "Invalid character in code: " + c);
        }
    }
}