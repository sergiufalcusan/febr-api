package io.febr.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.febr.api.domain.Course;
import io.febr.api.domain.Role;
import io.febr.api.domain.Teacher;
import io.febr.api.factory.CourseFactory;
import io.febr.api.integration.BaseIntegrationTest;
import io.febr.api.repository.CourseRepository;
import io.febr.api.repository.TeacherRepository;
import io.febr.api.repository.UserRepository;
import io.febr.api.service.CourseService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
public class CourseControllerTest extends BaseIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void tearDown() {
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    @Test
    public void testCreateCourse() throws Exception {
        Teacher teacher = Teacher.builder().email("teacher@gmail.com")
                .password("password")
                .firstName("teacher")
                .lastName("teacher")
                .role(Role.ROLE_TEACHER).build();
        teacherRepository.save(teacher);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/teacher/course/create")
                        .with(user("teacher@gmail.com").roles("TEACHER")).with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(CourseFactory.createRequestDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void testGetAllCourses() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        createCourse();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/course/all").with(user("user")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    private Teacher createTeacher() {
        Teacher teacher = Teacher.builder().email("teacher@gmail.com")
                .password("password")
                .firstName("teacher")
                .lastName("teacher")
                .role(Role.ROLE_TEACHER).build();

        return teacherRepository.save(teacher);
    }

    private void createCourse() {
        var teacher = createTeacher();
        Course course = Course.builder()
                .name("course")
                .description("description")
                .schedule(LocalDateTime.now())
                .teacher(teacher).build();
        courseRepository.save(course);
    }
}
