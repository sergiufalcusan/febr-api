package io.febr.api.dto;

public class StudentDTO {
    public record RegisterRequest(String email, String password, String firstName, String lastName) {
    }

    public record RegisterResponse(Long id, String email, String firstName, String lastName) {
    }
}
