package com.pawemie.flexlinkshortening.service;

import com.pawemie.flexlinkshortening.dto.ShortUrlResponse;
import com.pawemie.flexlinkshortening.exception.ShortUrlNotFoundException;
import com.pawemie.flexlinkshortening.login.appuser.AppUser;
import com.pawemie.flexlinkshortening.login.appuser.AppUserRepository;
import com.pawemie.flexlinkshortening.model.ShortUrl;
import com.pawemie.flexlinkshortening.repository.UrlRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.pawemie.flexlinkshortening.service.RandomBase62Generator.generateCode;
import static com.pawemie.flexlinkshortening.service.Utils.isValidCode;
import static com.pawemie.flexlinkshortening.service.Utils.isValidUrl;

@RequiredArgsConstructor
@Service
@Slf4j
public class UrlService {

    private final UrlRepository urlRepository;
    private final AppUserRepository appUserRepository;

    public String shortenUrl(String request, String customCode) {
        if (!isValidUrl(request)) {
            throw new IllegalArgumentException("Invalid URL format");
        }

        String code = resolveCode(customCode);

        AppUser user = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String userName = auth.getPrincipal().toString();
            user = appUserRepository.findByUsername(userName)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "Logged-in user was not found in the database: " + userName));
        }

        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setCode(code);
        shortUrl.setLongUrl(request);
        shortUrl.setCreatedAt(LocalDateTime.now());
        shortUrl.setClickCount(0);
        shortUrl.setUser(user);

        urlRepository.save(shortUrl);

        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/{code}")
                .buildAndExpand(code)
                .toUriString();
    }

    @Transactional
    public String resolveShortCode(String code) {
        ShortUrl shortUrl = urlRepository.findById(code)
                .orElseThrow(() -> new ShortUrlNotFoundException(code));

        shortUrl.setClickCount(shortUrl.getClickCount() + 1);
        urlRepository.save(shortUrl);

        return shortUrl.getLongUrl();
    }

    public List<ShortUrlResponse> getLinksForCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new AccessDeniedException("User must be logged in to view links");
        }

        String userName = auth.getPrincipal().toString();
        AppUser user = appUserRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Logged-in user not found: " + userName));

        return urlRepository.findAllByUser(user)
                .stream()
                .map(url -> new ShortUrlResponse(url.getCode(), url.getLongUrl(), url.getCreatedAt(), url.getClickCount()))
                .collect(Collectors.toList());
    }

    private String resolveCode(String customCode) {
        String code;
        if (isValidCode(customCode)) {
            if (urlRepository.findById(customCode).isPresent()) {
                throw new IllegalArgumentException("Custom code already exists: " + customCode);
            }
            code = customCode;
        } else {
            boolean exists;
            do {
                code = generateCode();
                exists = urlRepository.findById(code).isPresent();
                if (exists) {
                    log.warn("Generated code '{}' already exists in the database. Generating a new one...", code);
                }
            } while (exists);
        }
        return code;
    }
}
