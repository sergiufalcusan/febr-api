package io.febr.api.controller;

import io.febr.api.domain.Course;
import io.febr.api.domain.Role;
import io.febr.api.domain.Student;
import io.febr.api.domain.Teacher;
import io.febr.api.integration.BaseIntegrationTest;
import io.febr.api.repository.CourseRepository;
import io.febr.api.repository.StudentRepository;
import io.febr.api.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.HashSet;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
public class StudentCourseControllerTest extends BaseIntegrationTest {
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
    public void testGetAllTStudentCourses() throws Exception {
        createAndEnrollStudent("student@gmail.com");
        createAndEnrollStudent("student2@gmail.com");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/student/course/all")
                        .with(user("student@gmail.com").authorities(new SimpleGrantedAuthority("SCOPE_ROLE_STUDENT"))).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    private Teacher createTeacher() {
        return createTeacher("teacher@gmail.com");
    }

    private Teacher createTeacher(String name) {
        Teacher teacher = Teacher.builder().email(RandomString.make(10) + name)
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

    private void createAndEnrollStudent(String email) {
        var student = createStudent(email);
        var course = createCourse();
        if (course.getStudents() == null) {
            course.setStudents(new HashSet<>());
        }
        course.getStudents().add(student);
        courseRepository.save(course);

        if (student.getCourses() == null) {
            student.setCourses(new HashSet<>());
        }
        student.getCourses().add(course);
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
