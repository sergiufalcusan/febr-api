package io.febr.api.controller.teacher;

import io.febr.api.controller.exception.CourseNotFoundException;
import io.febr.api.dto.CourseDTO;
import io.febr.api.dto.StudentDTO;
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

    /**
     * Create a new course
     *
     * @param courseDTO course creation request
     * @return newly created course information
     */
    @PostMapping("/create")
    public ResponseEntity<CourseDTO.CreateResponse> createCourse(@RequestBody CourseDTO.CreateRequest courseDTO) {
        return ResponseEntity.ok(courseService.createCourse(courseDTO));
    }

    /**
     * Get all courses for current teacher
     *
     * @return list of courses
     */
    @GetMapping("/all")
    public ResponseEntity<List<CourseDTO.CreateResponse>> getAllCoursesForCurrentTeacher() {
        return ResponseEntity.ok(courseService.getAllCoursesForCurrentTeacher());
    }

    /**
     * Get course by id
     *
     * @param id course id
     * @return course information
     * @throws CourseNotFoundException throws if course is not found or if current user does not have access to this course
     */
    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO.CreateResponse> getCourseByIdAndCurrentUserRole(@PathVariable Long id) throws CourseNotFoundException {
        return ResponseEntity.ok(courseService.getCourseByIdAndCurrentUserRole(id));
    }

    /**
     * Update course by id
     *
     * @param id        course id
     * @param courseDTO course update request
     * @return updated course information
     * @throws CourseNotFoundException throws if course is not found or if current user does not have access to this course
     */
    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO.CreateResponse> updateCourse(@PathVariable Long id, @RequestBody CourseDTO.UpdateRequest courseDTO) throws CourseNotFoundException {
        return ResponseEntity.ok(courseService.updateCourse(id, courseDTO));
    }

    /**
     * Delete course by id
     *
     * @param id course id
     */
    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

    /**
     * Enroll student to course
     *
     * @param enrollmentDTO enrollment request
     */
    @PostMapping("/enroll")
    public void enrollStudent(@RequestBody CourseDTO.EnrollmentRequest enrollmentDTO) {
        courseService.enrollStudent(enrollmentDTO);
    }

    /**
     * Unenroll student from course
     *
     * @param enrollmentDTO unenrollment request
     */
    @PostMapping("/unenroll")
    public void unenrollStudent(@RequestBody CourseDTO.EnrollmentRequest enrollmentDTO) {
        courseService.unenrollStudent(enrollmentDTO);
    }

    /**
     * Get all students for course
     *
     * @param id course id
     * @return list of students
     */
    @GetMapping("/{id}/students")
    public ResponseEntity<List<StudentDTO.RegisterResponse>> getStudentsByCourseId(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getStudentsByCourseId(id));
    }
}
