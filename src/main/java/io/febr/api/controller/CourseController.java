package io.febr.api.controller;

import io.febr.api.dto.CourseDTO;
import io.febr.api.service.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/course")
@AllArgsConstructor
public class CourseController {
    private CourseService courseService;

    @GetMapping("/all")
    public ResponseEntity<List<CourseDTO.CreateResponse>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }
}
