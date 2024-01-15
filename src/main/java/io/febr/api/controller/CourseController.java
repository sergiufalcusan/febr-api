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

    @PostMapping("/create")
    public ResponseEntity<CourseDTO.CreateResponse> createCourse(@RequestBody CourseDTO.CreateRequest courseDTO) {
        return ResponseEntity.ok(courseService.createCourse(courseDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CourseDTO.CreateResponse>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}/enroll")
    public void enrollStudent(@PathVariable Long id, @RequestBody CourseDTO.EnrollmentRequest enrollmentDTO) {
        courseService.enrollStudent(id, enrollmentDTO);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<CourseDTO.CreateResponse> updateCourse(@PathVariable Long id, @RequestBody CourseDTO.UpdateRequest courseDTO) {
        return ResponseEntity.ok(courseService.updateCourse(id, courseDTO));
    }

}
