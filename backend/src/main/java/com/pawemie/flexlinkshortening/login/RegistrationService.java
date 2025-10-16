package com.pawemie.flexlinkshortening.login;

import com.pawemie.flexlinkshortening.dto.RegistrationRequest;
import com.pawemie.flexlinkshortening.login.appuser.AppUser;
import com.pawemie.flexlinkshortening.login.appuser.AppUserRole;
import com.pawemie.flexlinkshortening.login.appuser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;

    public ResponseEntity<?> register(RegistrationRequest request) {
        if (appUserService.existsByUsername(request.username())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Username is already taken!");
        }
        if (appUserService.existsByEmail(request.email())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Email is already in use!");
        }

        appUserService.signUpUser(
                new AppUser(
                        request.username(),
                        request.email(),
                        request.password(),
                        AppUserRole.USER
                )
        );
        return ResponseEntity.ok("User registered successfully!");
    }
}
