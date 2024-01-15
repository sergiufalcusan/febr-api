package io.febr.api.dto;

public class TeacherDTO {
    public record RegisterRequest(String email, String password, String firstName, String lastName) {
    }

    public record RegisterResponse(String email, String password, String firstName, String lastName) {
    }
}
