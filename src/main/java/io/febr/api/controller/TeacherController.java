package io.febr.api.controller;

import io.febr.api.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/teacher")
@AllArgsConstructor
public class TeacherController {
    public UserService userService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllCourses() {
        return ResponseEntity.ok(userService.getAllTeachers());
    }
}
