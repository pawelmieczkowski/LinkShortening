package com.pawemie.flexlinkshortening.service;

import com.pawemie.flexlinkshortening.dto.ShortUrlResponse;
import com.pawemie.flexlinkshortening.exception.ShortUrlNotFoundException;
import com.pawemie.flexlinkshortening.login.appuser.AppUser;
import com.pawemie.flexlinkshortening.login.appuser.AppUserRepository;
import com.pawemie.flexlinkshortening.model.ShortUrl;
import com.pawemie.flexlinkshortening.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {
    @Mock
    private UrlRepository urlRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private UrlService urlService;

    @BeforeEach
    void setup() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/shorten");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void shouldShortenValidUrlWithoutCustomCode() {
        String longUrl = "https://example.com";
        when(urlRepository.findById(anyString())).thenReturn(Optional.empty());

        String result = urlService.shortenUrl(longUrl, null);

        assertTrue(result.contains("/"));
        verify(urlRepository, atLeastOnce()).save(any(ShortUrl.class));
    }

    @Test
    void shouldRegenerateCodeIfFirstAlreadyExists() {
        String longUrl = "https://example.com";
        when(urlRepository.findById(anyString()))
                .thenReturn(Optional.of(new ShortUrl()))
                .thenReturn(Optional.empty());

        String result = urlService.shortenUrl(longUrl, null);

        assertTrue(result.contains("/"));
        verify(urlRepository, atLeast(2)).findById(anyString());
    }


    @Test
    void shouldShortenWithCustomCode() {
        String longUrl = "https://test.com";
        String customCode = "abc123";
        when(urlRepository.findById(customCode)).thenReturn(Optional.empty());

        String result = urlService.shortenUrl(longUrl, customCode);

        assertTrue(result.endsWith("/" + customCode));
        verify(urlRepository).save(any(ShortUrl.class));
    }

    @Test
    void shouldThrowWhenInvalidUrl() {
        assertThrows(IllegalArgumentException.class, () ->
                urlService.shortenUrl("not_a_url", null));
    }


    @Test
    void shouldThrowWhenCustomCodeExists() {
        when(urlRepository.findById("abc123")).thenReturn(Optional.of(new ShortUrl()));
        assertThrows(IllegalArgumentException.class, () ->
                urlService.shortenUrl("https://example.com", "abc123"));
    }

    @Test
    void shouldAssociateUserWhenAuthenticated() {
        AppUser user = new AppUser();
        user.setUsername("john");

        when(appUserRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(urlRepository.findById(anyString())).thenReturn(Optional.empty());

        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("john", "password", "ROLE_USER"));

        urlService.shortenUrl("https://example.com", null);

        verify(urlRepository).save(argThat(u -> u.getUser() != null));
    }

    @Test
    void shouldResolveExistingShortCode() {
        ShortUrl url = new ShortUrl();
        url.setCode("abc");
        url.setLongUrl("https://example.com");
        url.setClickCount(1);

        when(urlRepository.findById("abc")).thenReturn(Optional.of(url));

        String result = urlService.resolveShortCode("abc");

        assertEquals("https://example.com", result);
        assertEquals(2, url.getClickCount());
        verify(urlRepository).save(url);
    }

    @Test
    void shouldThrowWhenCodeNotFound() {
        when(urlRepository.findById("xyz")).thenReturn(Optional.empty());
        assertThrows(ShortUrlNotFoundException.class, () ->
                urlService.resolveShortCode("xyz"));
    }

    @Test
    void shouldReturnLinksForCurrentUser() {
        AppUser user = new AppUser();
        user.setUsername("john");

        ShortUrl url = new ShortUrl();
        url.setCode("abc");
        url.setLongUrl("https://example.com");
        url.setCreatedAt(LocalDateTime.now());
        url.setClickCount(5);

        ShortUrl url2 = new ShortUrl();
        url2.setCode("abcdd");
        url2.setLongUrl("https://example2.com");
        url2.setCreatedAt(LocalDateTime.now());
        url2.setClickCount(3);

        when(appUserRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(urlRepository.findAllByUser(user)).thenReturn(List.of(url, url2));

        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("john", "password", "ROLE_USER"));

        List<ShortUrlResponse> responses = urlService.getLinksForCurrentUser();

        assertEquals(2, responses.size());
        assertEquals("abc", responses.getFirst().code());
    }


    @Test
    void shouldThrowWhenUserNotLoggedIn() {
        SecurityContextHolder.clearContext();
        assertThrows(AccessDeniedException.class, () -> urlService.getLinksForCurrentUser());
    }
}