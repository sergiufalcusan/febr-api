package io.febr.api.dto;

public class AuthDTO {
    public record LoginRequest(String username, String password) {
    }

    public record LoginResponse(String message, String token) {
    }
}