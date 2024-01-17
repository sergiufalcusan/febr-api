package io.febr.api.domain;

/**
 * The Role enum.
 * For this POC we will only have 3 roles: ROLE_TEACHER, ROLE_STUDENT, ROLE_ADMIN
 * A user can have only one role in this POC, but in a real application a user should have multiple authorities.
 */
public enum Role {
    ROLE_TEACHER,
    ROLE_STUDENT,
    ROLE_ADMIN
}
