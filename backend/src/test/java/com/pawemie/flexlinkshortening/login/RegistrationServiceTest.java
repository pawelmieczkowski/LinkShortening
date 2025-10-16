package com.pawemie.flexlinkshortening.login;

import com.pawemie.flexlinkshortening.dto.RegistrationRequest;
import com.pawemie.flexlinkshortening.login.appuser.AppUserRole;
import com.pawemie.flexlinkshortening.login.appuser.AppUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private AppUserService appUserService;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void shouldReturnBadRequestIfUsernameExists() {
        RegistrationRequest request = new RegistrationRequest("john", "john@example.com", "pass123");
        when(appUserService.existsByUsername("john")).thenReturn(true);

        ResponseEntity<?> response = registrationService.register(request);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Error: Username is already taken!", response.getBody());
        verify(appUserService, never()).signUpUser(any());
    }

    @Test
    void shouldReturnBadRequestIfEmailExists() {
        RegistrationRequest request = new RegistrationRequest("john", "pass123", "john@example.com");
        when(appUserService.existsByUsername("john")).thenReturn(false);
        when(appUserService.existsByEmail("john@example.com")).thenReturn(true);

        ResponseEntity<?> response = registrationService.register(request);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Error: Email is already in use!", response.getBody());
        verify(appUserService, never()).signUpUser(any());
    }


    @Test
    void shouldRegisterUserWhenUsernameAndEmailAreFree() {
        RegistrationRequest request = new RegistrationRequest("john", "pass123", "john@example.com");
        when(appUserService.existsByUsername("john")).thenReturn(false);
        when(appUserService.existsByEmail("john@example.com")).thenReturn(false);

        ResponseEntity<?> response = registrationService.register(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("User registered successfully!", response.getBody());
        verify(appUserService).signUpUser(argThat(user ->
                user.getUsername().equals("john") &&
                        user.getEmail().equals("john@example.com") &&
                        user.getPassword().equals("pass123") &&
                        user.getAppUserRole() == AppUserRole.USER
        ));
    }

}