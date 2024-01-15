package io.febr.api.integration;

import io.febr.api.domain.Course;
import io.febr.api.domain.Role;
import io.febr.api.domain.Student;
import io.febr.api.domain.Teacher;
import io.febr.api.dto.CourseDTO;
import io.febr.api.repository.StudentRepository;
import io.febr.api.factory.CourseFactory;
import io.febr.api.repository.CourseRepository;
import io.febr.api.repository.TeacherRepository;
import io.febr.api.service.CourseService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
public class CourseIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @AfterEach
    public void tearDown() {
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    @Test
    @WithMockUser(value = "teacher@gmail.com", roles = "TEACHER")
    public void testCreateCourse() {
        createDefaultTeacher();

        CourseDTO.CreateResponse courseDTO = courseService.createCourse(CourseFactory.createRequestDTO());
        assertThat(courseDTO).isNotNull();
        assertThat(courseRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @WithMockUser(value = "teacher@gmail.com", roles = "TEACHER")
    public void testGetAllCourses() {
        createDefaultTeacher();

        courseService.createCourse(CourseFactory.createRequestDTO());
        courseService.createCourse(CourseFactory.createRequestDTO());

        List<CourseDTO.CreateResponse> courseDTOList = courseService.getAllCourses();
        assertThat(courseDTOList.size()).isEqualTo(2);
    }

    @Test
    public void testGetAllCoursesEmptyList() {
        List<CourseDTO.CreateResponse> courseDTOList = courseService.getAllCourses();
        assertThat(courseDTOList.size()).isEqualTo(0);
    }

    @Test
    @WithMockUser(value = "teacher@gmail.com", roles = "TEACHER")
    public void testGetCourseById() {
        createDefaultTeacher();

        var courseDto = courseService.createCourse(CourseFactory.createRequestDTO());

        CourseDTO.CreateResponse courseDTO = courseService.getCourseById(courseDto.id());
        assertThat(courseDTO).isNotNull();
    }

    @Test
    public void testGetCourseByIdNotFound() {
        Exception exception = assertThrows(NoSuchElementException.class, () -> courseService.getCourseById(1L));
        assertThat(exception.getMessage()).isEqualTo("No value present");
    }

    @Test
    @WithMockUser(value = "teacher@gmail.com", roles = "TEACHER")
    public void testUpdateCourse() {
        createDefaultTeacher();
        var teacher2 = createTeacher("teacher2@gmail.com");
        var courseDto = courseService.createCourse(CourseFactory.createRequestDTO());

        String COURSE_NAME = "Course 2";
        String COURSE_DESCRIPTION = "Description 2";
        LocalDateTime COURSE_SCHEDULE = LocalDateTime.now();
        Long TEACHER_ID = teacher2.getId();


        CourseDTO.CreateResponse courseDTO = courseService.updateCourse(courseDto.id(),
                CourseDTO.UpdateRequest.builder()
                        .name(COURSE_NAME)
                        .description(COURSE_DESCRIPTION)
                        .schedule(COURSE_SCHEDULE)
                        .teacherId(TEACHER_ID)
                        .build()
        );

        Course updatedCourse = courseRepository.findById(courseDto.id()).orElseThrow();

        assertThat(updatedCourse).isNotNull();
        assertThat(updatedCourse.getName()).isEqualTo(COURSE_NAME);
        assertThat(updatedCourse.getDescription()).isEqualTo(COURSE_DESCRIPTION);
        assertThat(updatedCourse.getSchedule()).isEqualTo(COURSE_SCHEDULE);
        assertThat(updatedCourse.getTeacher().getId()).isEqualTo(teacher2.getId());
    }

    @Test
    @WithMockUser(value = "teacher@gmail.com", roles = "TEACHER")
    public void testDeleteCourse() {
        createDefaultTeacher();
        var courseDto = courseService.createCourse(CourseFactory.createRequestDTO());

        courseService.deleteCourse(courseDto.id());
        assertThat(courseRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @WithMockUser(value = "teacher@gmail.com", roles = "TEACHER")
    public void testDeleteAllCourses() {
        createDefaultTeacher();
        courseService.createCourse(CourseFactory.createRequestDTO());

        courseService.deleteAllCourses();
        assertThat(courseRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @WithMockUser(value = "teacher@gmail.com", roles = "TEACHER")
    public void testEnrollStudent() {
        createDefaultTeacher();
        var courseDto = courseService.createCourse(CourseFactory.createRequestDTO());

        var student = createStudent("student@gmail.com");

        courseService.enrollStudent(courseDto.id(), new CourseDTO.EnrollmentRequest(student.getId()));

        var course = courseRepository.findById(courseDto.id()).orElseThrow();
        assertThat(course.getStudents().size()).isEqualTo(1);
    }

    @Test
    @WithMockUser(value = "teacher@gmail.com", roles = "TEACHER")
    public void testEnrollSameStudentTwice() {
        createDefaultTeacher();
        var courseDto = courseService.createCourse(CourseFactory.createRequestDTO());

        var student = createStudent("student@gmail.com");

        courseService.enrollStudent(courseDto.id(), new CourseDTO.EnrollmentRequest(student.getId()));
        courseService.enrollStudent(courseDto.id(), new CourseDTO.EnrollmentRequest(student.getId()));

        var course = courseRepository.findById(courseDto.id()).orElseThrow();
        assertThat(course.getStudents().size()).isEqualTo(1);
    }

    @Test
    @WithMockUser(value = "teacher@gmail.com", roles = "TEACHER")
    public void testEnrollMultipleUsers() {
        createDefaultTeacher();
        var courseDto = courseService.createCourse(CourseFactory.createRequestDTO());

        var student1 = createStudent("student1@gmail.com");
        var student2 = createStudent("student2@gmail.com");

        courseService.enrollStudent(courseDto.id(), new CourseDTO.EnrollmentRequest(student1.getId()));
        courseService.enrollStudent(courseDto.id(), new CourseDTO.EnrollmentRequest(student2.getId()));

        var course = courseRepository.findById(courseDto.id()).orElseThrow();
        assertThat(course.getStudents().size()).isEqualTo(2);
    }

    private Teacher createDefaultTeacher() {
        return createTeacher("teacher@gmail.com");
    }

    private Teacher createTeacher(String email) {
        Teacher teacher = Teacher.builder()
                .email(email)
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
