package io.febr.api.dto;

/**
 * The TeacherDTO class.
 * This class contains all the DTOs related to teachers.
 */
public class TeacherDTO {
    public record RegisterRequest(String email, String password, String firstName, String lastName) {
    }

    public record RegisterResponse(String email, String firstName, String lastName) {
    }
}
