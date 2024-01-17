package io.febr.api.dto;

/**
 * The AuthDTO class.
 * This class contains all the DTOs related to authentication.
 */
public class AuthDTO {
    public record LoginRequest(String username, String password) {
    }

    public record LoginResponse(String message, String token) {
    }

    public record UserResponse(Long id, String email, String firstName, String lastName, String role) {
    }
}