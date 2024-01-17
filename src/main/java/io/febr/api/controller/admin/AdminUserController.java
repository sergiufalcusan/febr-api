package io.febr.api.controller.admin;

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
@RequestMapping("/api/v1/admin/users")
@AllArgsConstructor
public class AdminUserController {
    UserService userService;
    AuthorizationService authorizationService;

    /**
     * Create a new teacher
     *
     * @param registerDTO teacher registration request
     * @return newly created teacher information
     */
    @PostMapping("/teacher/new")
    public ResponseEntity<?> createTeacher(@RequestBody TeacherDTO.RegisterRequest registerDTO) {
        return ResponseEntity.ok(userService.createTeacher(registerDTO));
    }
}
