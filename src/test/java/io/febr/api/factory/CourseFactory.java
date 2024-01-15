package io.febr.api.factory;

import io.febr.api.dto.CourseDTO;

import java.time.LocalDateTime;

public class CourseFactory {
    private CourseFactory() {
    }

    public static CourseDTO.CreateResponse createResponseDTO() {
        return new CourseDTO.CreateResponse(1L, "Course 1", "Description 1", LocalDateTime.now());
    }

    public static CourseDTO.CreateRequest createRequestDTO() {
        return new CourseDTO.CreateRequest("Course 1", "Description 1", LocalDateTime.now());
    }

    public static CourseDTO.UpdateRequest updateRequestDTO() {
        return new CourseDTO.UpdateRequest("Course 2", "Description 1", LocalDateTime.now(), 1L);
    }
}
