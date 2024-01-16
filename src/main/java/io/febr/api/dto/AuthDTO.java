package io.febr.api.dto;

public class AuthDTO {
    public record LoginRequest(String username, String password) {
    }

    public record LoginResponse(String message, String token) {
    }

    public record UserResponse(Long id, String email, String firstName, String lastName, String role) {
    }
}