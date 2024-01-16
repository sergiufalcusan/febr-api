package io.febr.api.controller.teacher;

import io.febr.api.dto.CourseDTO;
import io.febr.api.service.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teacher/course")
@AllArgsConstructor
public class TeacherCourseController {
    private CourseService courseService;

    @PostMapping("/create")
    public ResponseEntity<CourseDTO.CreateResponse> createCourse(@RequestBody CourseDTO.CreateRequest courseDTO) {
        return ResponseEntity.ok(courseService.createCourse(courseDTO));
    }

    @PostMapping("/enroll")
    public void enrollStudent(@RequestBody CourseDTO.EnrollmentRequest enrollmentDTO) {
        courseService.enrollStudent(enrollmentDTO);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<CourseDTO.CreateResponse> updateCourse(@PathVariable Long id, @RequestBody CourseDTO.UpdateRequest courseDTO) {
        return ResponseEntity.ok(courseService.updateCourse(id, courseDTO));
    }
}
