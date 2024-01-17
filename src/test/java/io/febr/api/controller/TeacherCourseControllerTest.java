package io.febr.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.febr.api.domain.Course;
import io.febr.api.domain.Role;
import io.febr.api.domain.Teacher;
import io.febr.api.factory.CourseFactory;
import io.febr.api.integration.BaseIntegrationTest;
import io.febr.api.repository.CourseRepository;
import io.febr.api.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
public class TeacherCourseControllerTest extends BaseIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @AfterEach
    public void tearDown() {
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    @Test
    public void testCreateCourse() throws Exception {
        createTeacher();
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/teacher/course/create")
                        .with(user("teacher@gmail.com").authorities(new SimpleGrantedAuthority("SCOPE_ROLE_TEACHER"))).with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(CourseFactory.createRequestDTO())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void testGetAllTeacherCourses() throws Exception {
        createCourse();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/teacher/course/all")
                        .with(user("teacher@gmail.com").authorities(new SimpleGrantedAuthority("SCOPE_ROLE_TEACHER"))).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testGetTeacherCourseById() throws Exception {
        var course = createCourse();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/teacher/course/" + course.getId())
                        .with(user("teacher@gmail.com").authorities(new SimpleGrantedAuthority("SCOPE_ROLE_TEACHER"))).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(course.getId()));
    }

    @Test
    public void testUpdateTeacherCourse() throws Exception {
        var course = createCourse();
        var objectMapper = new ObjectMapper();
        var updatedCourse = CourseFactory.updateRequestDTO();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/teacher/course/" + course.getId())
                        .with(user("teacher@gmail.com").authorities(new SimpleGrantedAuthority("SCOPE_ROLE_TEACHER"))).with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedCourse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(course.getId()))
                .andExpect(jsonPath("$.name").value(updatedCourse.getName()));
    }

    @Test
    public void updateOtherTeacherCourse() throws Exception {
        createTeacher("teacher2@gmail.com");
        var course = createCourse();
        var objectMapper = new ObjectMapper();
        var updatedCourse = CourseFactory.updateRequestDTO();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/teacher/course/" + course.getId())
                        .with(user("teacher2@gmail.com").authorities(new SimpleGrantedAuthority("SCOPE_ROLE_TEACHER"))).with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedCourse)))
                .andExpect(status().isNotFound());

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

    private Course createCourse() {
        var teacher = createTeacher();
        Course course = Course.builder()
                .name("course")
                .description("description")
                .schedule(LocalDateTime.now())
                .teacher(teacher).build();
        return courseRepository.save(course);
    }
}
