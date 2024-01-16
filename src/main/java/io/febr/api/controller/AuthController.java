package io.febr.api.controller;

import io.febr.api.dto.AuthDTO;
import io.febr.api.service.AuthorizationService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private AuthorizationService authService;
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO.LoginRequest userLogin) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLogin.username(),
                        userLogin.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Token requested for user :{}", authentication.getAuthorities());
        String token = authService.generateToken(authentication);

        AuthDTO.LoginResponse response = new AuthDTO.LoginResponse("User logged in successfully", token);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        return ResponseEntity.ok(authService.getCurrentUser());
    }
}
