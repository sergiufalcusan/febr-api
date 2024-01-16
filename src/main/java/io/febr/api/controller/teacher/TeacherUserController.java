package io.febr.api.controller.teacher;

import io.febr.api.dto.StudentDTO;
import io.febr.api.dto.TeacherDTO;
import io.febr.api.service.AuthorizationService;
import io.febr.api.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/teacher/users")
@AllArgsConstructor
public class TeacherUserController {
    UserService userService;
    AuthorizationService authorizationService;

    @PostMapping("/student/new")
    public ResponseEntity<?> createStudent(@RequestBody StudentDTO.RegisterRequest registerDTO) {
        return ResponseEntity.ok(userService.createStudent(registerDTO));
    }
}
