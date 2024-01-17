package io.febr.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.febr.api.domain.Role;
import io.febr.api.domain.Student;
import io.febr.api.domain.Teacher;
import io.febr.api.dto.StudentDTO;
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
public class TeacherUserController extends BaseIntegrationTest {
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
    public void testCreateStudent() throws Exception {
        var objectMapper = new ObjectMapper();
        createTeacher();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/teacher/users/student/new")
                        .with(user("teacher@gmail.com").authorities(new SimpleGrantedAuthority("SCOPE_ROLE_TEACHER"))).with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new StudentDTO.RegisterRequest("student@gmail.com", "password", "student", "student")))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.email").value("student@gmail.com"));
    }

    private Teacher createTeacher() {
        return createTeacher("teacher@gmail.com");
    }

    private Teacher createTeacher(String name) {
        Teacher teacher = Teacher.builder().email(name)
                .password("password")
                .firstName("teacher")
                .lastName("teacher")
                .role(Role.ROLE_TEACHER).build();

        return teacherRepository.save(teacher);
    }

    private Student createStudent(String email) {
        Student student = Student.builder()
                .email(email)
                .password("password")
                .firstName("student")
                .lastName("student")
                .role(Role.ROLE_STUDENT).build();

        return studentRepository.save(student);
    }
}
