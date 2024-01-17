package io.febr.api.controller.student;

import io.febr.api.controller.exception.CourseNotFoundException;
import io.febr.api.dto.CourseDTO;
import io.febr.api.service.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student/course")
@AllArgsConstructor
public class StudentCourseController {
    private CourseService courseService;

    /**
     * Get all courses for current student
     *
     * @return list of courses
     */
    @GetMapping("/all")
    public ResponseEntity<List<CourseDTO.CreateResponse>> getAllCoursesForCurrentStudent() {
        return ResponseEntity.ok(courseService.getAllCoursesForCurrentStudent());
    }

    /**
     * Get course by id
     *
     * @param id course id
     * @return course information
     * @throws CourseNotFoundException throws if course is not found or if current user does not have access to this course
     */
    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO.CreateResponse> getCourseById(@PathVariable Long id) throws CourseNotFoundException {
        return ResponseEntity.ok(courseService.getCourseByIdAndCurrentUserRole(id));
    }
}
