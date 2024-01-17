package io.febr.api.controller;

import io.febr.api.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UsersController {
    public UserService userService;

    /**
     * Get all teachers
     *
     * @return list of teachers
     */
    @GetMapping("/teachers")
    public ResponseEntity<?> getAllTeachers() {
        return ResponseEntity.ok(userService.getAllTeachers());
    }

    /**
     * Get all students
     *
     * @return list of students
     */
    @GetMapping("/students")
    public ResponseEntity<?> getAllStudents() {
        return ResponseEntity.ok(userService.getAllStudents());
    }
}
