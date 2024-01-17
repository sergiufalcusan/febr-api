package io.febr.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.febr.api.domain.Role;
import io.febr.api.domain.Teacher;
import io.febr.api.dto.TeacherDTO;
import io.febr.api.integration.BaseIntegrationTest;
import io.febr.api.repository.CourseRepository;
import io.febr.api.repository.StudentRepository;
import io.febr.api.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
public class AdminUserController extends BaseIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @AfterEach
    public void tearDown() {
        studentRepository.deleteAll();
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    @Test
    public void testCreateAdmin() throws Exception {
        var objectMapper = new ObjectMapper();
        createAdmin();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/users/teacher/new")
                        .with(user("admin@gmail.com").authorities(new SimpleGrantedAuthority("SCOPE_ROLE_ADMIN"))).with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new TeacherDTO.RegisterRequest("teacher@gmail.com", "password", "teacher", "teacher")))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("teacher@gmail.com"));
    }

    private Teacher createAdmin() {
        return createAdmin("admin@gmail.com");
    }

    private Teacher createAdmin(String name) {
        Teacher teacher = Teacher.builder().email(name)
                .password("password")
                .firstName("admin")
                .lastName("admin")
                .role(Role.ROLE_ADMIN).build();

        return teacherRepository.save(teacher);
    }
}
