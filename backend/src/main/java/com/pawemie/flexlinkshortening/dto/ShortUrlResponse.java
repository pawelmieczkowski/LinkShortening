package com.pawemie.flexlinkshortening.dto;

import java.time.LocalDateTime;

public record ShortUrlResponse(String code,
                               String longUrl,
                               LocalDateTime createdAt,
                               long clickCount) {
}
