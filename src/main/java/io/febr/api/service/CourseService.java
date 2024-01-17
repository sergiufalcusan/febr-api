package io.febr.api.service;

import io.febr.api.controller.exception.CourseNotFoundException;
import io.febr.api.domain.*;
import io.febr.api.dto.CourseDTO;
import io.febr.api.dto.StudentDTO;
import io.febr.api.mapper.CourseMapper;
import io.febr.api.mapper.StudentMapper;
import io.febr.api.repository.CourseRepository;
import io.febr.api.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
@Transactional
public class CourseService {
    private static final Logger log = LoggerFactory.getLogger(CourseService.class);
    private CourseRepository courseRepository;
    private StudentRepository studentRepository;
    private UserService userService;
    private CourseMapper courseMapper;
    private StudentMapper studentMapper;
    private EmailService emailService;

    /**
     * Get all courses
     *
     * @return list of courses
     */
    public List<CourseDTO.CreateResponse> getAllCourses() {
        return courseMapper.toDtoList(courseRepository.findAll());
    }

    /**
     * Get all courses for current teacher
     *
     * @return a list of courses
     */
    public List<CourseDTO.CreateResponse> getAllCoursesForCurrentTeacher() {
        User currentUser = userService.getCurrentUser();
        return courseMapper.toDtoList(courseRepository.findAllByTeacherId(currentUser.getId()));
    }

    /**
     * Get all courses for current student
     *
     * @return a list of courses
     */
    public List<CourseDTO.CreateResponse> getAllCoursesForCurrentStudent() {
        User currentUser = userService.getCurrentUser();
        return courseMapper.toDtoList(courseRepository.findAllByStudentId(currentUser.getId()));
    }

    /**
     * Get course by id, depending on current user role and if it's related to the user
     *
     * @param id course id
     * @return course
     * @throws CourseNotFoundException if course not found or user has no access to it
     */
    public CourseDTO.CreateResponse getCourseByIdAndCurrentUserRole(Long id) throws CourseNotFoundException {
        User currentUser = userService.getCurrentUser();

        try {
            if (currentUser.getRole() == Role.ROLE_TEACHER) {
                return courseMapper.toDto(courseRepository.findByIdAndTeacherId(id, currentUser.getId()).orElseThrow());
            } else if (currentUser.getRole() == Role.ROLE_STUDENT) {
                return courseMapper.toDto(courseRepository.findByIdAndStudentId(id, currentUser.getId()).orElseThrow());
            } else {
                throw new RuntimeException("Unknown user type");
            }
        } catch (NoSuchElementException e) {
            throw new CourseNotFoundException();
        }
    }

    /**
     * Create a course
     *
     * @param courseDTO course data containing name, description and schedule
     * @return created course
     */
    public CourseDTO.CreateResponse createCourse(CourseDTO.CreateRequest courseDTO) {
        Teacher currentUser = (Teacher) userService.getCurrentUser();
        Course course = courseMapper.createDtoToEntity(courseDTO);
        course.setTeacher(currentUser);
        return courseMapper.toDto(courseRepository.save(course));
    }

    /**
     * Update a course
     *
     * @param id        course id
     * @param courseDTO course data containing name, description and schedule
     * @return updated course
     * @throws CourseNotFoundException if course not found or user has no access to it
     */
    public CourseDTO.CreateResponse updateCourse(Long id, CourseDTO.UpdateRequest courseDTO) throws CourseNotFoundException {
        User teacher = userService.getCurrentUser();
        Course course = courseRepository.findByIdAndTeacherId(id, teacher.getId()).orElseThrow(CourseNotFoundException::new);

        if (courseDTO.getName() != null) {
            course.setName(courseDTO.getName());
        }

        if (courseDTO.getDescription() != null) {
            course.setDescription(courseDTO.getDescription());
        }

        if (courseDTO.getSchedule() != null) {
            course.setSchedule(courseDTO.getSchedule());
        }

        return courseMapper.toDto(courseRepository.save(course));
    }

    /**
     * Delete a course
     *
     * @param id course id
     */
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    /**
     * Delete all courses
     */
    public void deleteAllCourses() {
        courseRepository.deleteAll();
    }

    /**
     * Enroll a student in a course
     *
     * @param courseEnrollmentDTO course id and student id
     */
    public void enrollStudent(CourseDTO.EnrollmentRequest courseEnrollmentDTO) {
        Course course = courseRepository.findById(courseEnrollmentDTO.courseId()).orElseThrow();
        Student student = studentRepository.findById(courseEnrollmentDTO.studentId()).orElseThrow();

        if (course.getStudents() == null) {
            course.setStudents(new HashSet<>(List.of(student)));
        } else {
            course.getStudents().add(student);
        }

        if (student.getCourses() == null) {
            student.setCourses(new HashSet<>(List.of(course)));
        } else {
            student.getCourses().add(course);
        }

        courseRepository.save(course);
        studentRepository.save(student);

        try {
            if (student.getEmail() != null) {
                emailService.sendEmail(
                        "Successfully enrolled!",
                        "You have been enrolled in a course: " + course.getName(),
                        student.getEmail()
                );
            }
        } catch (Exception e) {
            log.error("Error sending email to student: " + student.getEmail());
        }
    }

    /**
     * Unenroll a student from a course
     *
     * @param courseEnrollmentDTO course id and student id
     */
    public void unenrollStudent(CourseDTO.EnrollmentRequest courseEnrollmentDTO) {
        Course course = courseRepository.findById(courseEnrollmentDTO.courseId()).orElseThrow();
        Student student = studentRepository.findById(courseEnrollmentDTO.studentId()).orElseThrow();

        if (course.getStudents() == null) {
            course.setStudents(new HashSet<>(List.of(student)));
        } else {
            course.getStudents().remove(student);
        }

        if (student.getCourses() == null) {
            student.setCourses(new HashSet<>(List.of(course)));
        } else {
            student.getCourses().remove(course);
        }

        courseRepository.save(course);
        studentRepository.save(student);
    }

    /**
     * Get all students enrolled in a course
     *
     * @param id course id
     * @return list of students
     */
    public List<StudentDTO.RegisterResponse> getStudentsByCourseId(Long id) {
        Course course = courseRepository.findById(id).orElseThrow();
        return studentMapper.toDtoList(course.getStudents().stream().toList());
    }
}
