package com.pawemie.flexlinkshortening.login.appuser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AppUserService appUserService;

    private AppUser testUser;

    @BeforeEach
    void setup() {
        testUser = new AppUser();
        testUser.setUsername("john");
        testUser.setEmail("john@example.com");
        testUser.setPassword("plainPassword");
    }

    @Test
    void shouldLoadUserByUsernameIfExists() {
        when(appUserRepository.findByEmail("john")).thenReturn(Optional.empty());
        when(appUserRepository.findByUsername("john")).thenReturn(Optional.of(testUser));

        AppUser user = appUserService.loadUserByUsername("john");

        assertNotNull(user);
        assertEquals("john", user.getUsername());
    }

    @Test
    void shouldThrowIfUserNotFound() {
        when(appUserRepository.findByEmail("unknown")).thenReturn(Optional.empty());
        when(appUserRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        UsernameNotFoundException ex = assertThrows(
                UsernameNotFoundException.class,
                () -> appUserService.loadUserByUsername("unknown")
        );

        assertTrue(ex.getMessage().contains("user with email unknown not found"));
    }

    @Test
    void shouldSignUpUserWhenEmailNotRegistered() {
        when(appUserRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(appUserRepository.save(any(AppUser.class))).thenReturn(testUser);

        AppUser savedUser = appUserService.signUpUser(testUser);

        assertEquals(testUser, savedUser);
        assertEquals("encodedPassword", savedUser.getPassword());
        verify(appUserRepository).save(testUser);
    }

    @Test
    void shouldThrowIfEmailAlreadyRegistered() {
        when(appUserRepository.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> appUserService.signUpUser(testUser)
        );

        assertEquals("email already registered", ex.getMessage());
    }

}