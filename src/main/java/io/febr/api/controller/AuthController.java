package io.febr.api.controller;

import io.febr.api.dto.AuthDTO;
import io.febr.api.service.AuthorizationService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private AuthorizationService authService;

    /**
     * Login user
     *
     * @param userLogin user login request containing username and password
     * @return login response containing message and JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO.LoginRequest userLogin) {
        String token = authService.generateToken(userLogin);
        return ResponseEntity.ok(new AuthDTO.LoginResponse("User logged in successfully", token));
    }

    /**
     * Fetch current user information from JWT token
     *
     * @return current user information
     */
    @GetMapping("/me")
    public ResponseEntity<?> me() {
        return ResponseEntity.ok(authService.getCurrentUser());
    }
}
