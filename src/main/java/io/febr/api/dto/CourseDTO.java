package io.febr.api.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTOs for Course
 */
public class CourseDTO {
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    @JsonSerialize
    public record CreateRequest(
            String name,
            String description,
            @JsonSerialize(using = LocalDateTimeSerializer.class) LocalDateTime schedule
    ) {
    }

    public record CreateResponse(
            Long id,
            String name,
            String description,
            LocalDateTime schedule
    ) {
    }

    @Data
    @AllArgsConstructor
    @Builder
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    @JsonSerialize
    public static class UpdateRequest {
        private String name;
        private String description;
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private LocalDateTime schedule;
    }

    public record EnrollmentRequest(Long courseId, Long studentId) {
    }
}
