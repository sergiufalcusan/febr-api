package io.febr.api.dto;

/**
 * The StudentDTO class.
 * This class contains all the DTOs related to students.
 */
public class StudentDTO {
    public record RegisterRequest(String email, String password, String firstName, String lastName) {
    }

    public record RegisterResponse(Long id, String email, String firstName, String lastName) {
    }
}
