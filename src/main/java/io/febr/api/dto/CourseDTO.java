package io.febr.api.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    ) { }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class UpdateRequest {
        private String name;
        private String description;
        private LocalDateTime schedule;
        private Long teacherId;
    }

    public record EnrollmentRequest(Long courseId, Long studentId) { }
}
