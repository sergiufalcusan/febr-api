package io.febr.api.service;

import io.febr.api.domain.Course;
import io.febr.api.domain.Student;
import io.febr.api.domain.Teacher;
import io.febr.api.dto.CourseDTO;
import io.febr.api.mapper.CourseMapper;
import io.febr.api.repository.CourseRepository;
import io.febr.api.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class CourseService {
    private CourseRepository courseRepository;
    private StudentRepository studentRepository;
    private UserService userService;
    private CourseMapper courseMapper;

    public List<CourseDTO.CreateResponse> getAllCourses() {
        return courseMapper.toDtoList(courseRepository.findAll());
    }

    public CourseDTO.CreateResponse getCourseById(Long id) {
        return courseMapper.toDto(courseRepository.findById(id).orElseThrow());
    }

    public CourseDTO.CreateResponse createCourse(CourseDTO.CreateRequest courseDTO) {
        Teacher currentUser = userService.getCurrentTeacherUser();
        Course course = courseMapper.createDtoToEntity(courseDTO);
        course.setTeacher(currentUser);
        return courseMapper.toDto(courseRepository.save(course));
    }

    public CourseDTO.CreateResponse updateCourse(Long id, CourseDTO.UpdateRequest courseDTO) {
        Course course = courseRepository.findById(id).orElseThrow();

        if (courseDTO.getName() != null) {
            course.setName(courseDTO.getName());
        }

        if (courseDTO.getDescription() != null) {
            course.setDescription(courseDTO.getDescription());
        }

        if (courseDTO.getSchedule() != null) {
            course.setSchedule(courseDTO.getSchedule());
        }

        if (courseDTO.getTeacherId() != null) {
            Teacher teacher = userService.getTeacherById(courseDTO.getTeacherId());
            course.setTeacher(teacher);
        }

        return courseMapper.toDto(courseRepository.save(course));
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public void deleteAllCourses() {
        courseRepository.deleteAll();
    }

    public void enrollStudent(Long id, CourseDTO.EnrollmentRequest courseEnrollmentDTO) {
        Course course = courseRepository.findById(id).orElseThrow();

        Student student = studentRepository.findById(courseEnrollmentDTO.studentId()).orElseThrow();

        if (course.getStudents() == null) {
            course.setStudents(new HashSet<>(List.of(student)));
        } else {
            course.getStudents().add(student);
        }

        courseRepository.save(course);
    }
}
