package com.pawemie.flexlinkshortening.controller;

import com.pawemie.flexlinkshortening.dto.ShortUrlResponse;
import com.pawemie.flexlinkshortening.dto.UrlRequest;
import com.pawemie.flexlinkshortening.dto.UrlResponse;
import com.pawemie.flexlinkshortening.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class UrlController {

    private final UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<UrlResponse> shortenUrl(@RequestBody UrlRequest request) {
        String shortUrl = urlService.shortenUrl(request.url(), request.customCode());
        return ResponseEntity.ok(new UrlResponse(shortUrl));
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirectToFullUrl(@PathVariable String code) {
        String url = urlService.resolveShortCode(code);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }

    @GetMapping("/private/my-links")
    public ResponseEntity<List<ShortUrlResponse>> getUserLinks() {
        List<ShortUrlResponse> links = urlService.getLinksForCurrentUser();
        return ResponseEntity.ok(links);
    }


}
