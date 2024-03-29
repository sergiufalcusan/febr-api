package io.febr.api.service;

import io.febr.api.domain.Role;
import io.febr.api.domain.Student;
import io.febr.api.domain.Teacher;
import io.febr.api.domain.User;
import io.febr.api.dto.StudentDTO;
import io.febr.api.dto.TeacherDTO;
import io.febr.api.mapper.StudentMapper;
import io.febr.api.mapper.TeacherMapper;
import io.febr.api.repository.StudentRepository;
import io.febr.api.repository.TeacherRepository;
import io.febr.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class UserService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private TeacherRepository teacherRepository;
    private PasswordEncoder passwordEncoder;
    private TeacherMapper teacherMapper;
    private StudentMapper studentMapper;

    /**
     * Get current user
     *
     * @return current user
     */
    protected User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName()).orElseThrow();
    }

    /**
     * Create teacher
     *
     * @param registerDTO teacher register request
     * @return teacher register response
     */
    public TeacherDTO.RegisterResponse createTeacher(TeacherDTO.RegisterRequest registerDTO) {
        Teacher teacher = Teacher.builder()
                .firstName(registerDTO.firstName())
                .lastName(registerDTO.lastName())
                .email(registerDTO.email())
                .password(passwordEncoder.encode(registerDTO.password()))
                .role(Role.ROLE_TEACHER)
                .build();

        teacher = teacherRepository.save(teacher);
        return teacherMapper.toDto(teacher);
    }

    /**
     * Get all teachers
     *
     * @return list of teachers
     */
    public List<TeacherDTO.RegisterResponse> getAllTeachers() {
        return teacherMapper.toDtoList(teacherRepository.findAll());
    }

    /**
     * Get all students
     *
     * @return list of students
     */
    public List<StudentDTO.RegisterResponse> getAllStudents() {
        return studentMapper.toDtoList(studentRepository.findAll());
    }

    /**
     * Create student
     *
     * @param registerDTO student register request
     * @return student register response
     */
    public StudentDTO.RegisterResponse createStudent(StudentDTO.RegisterRequest registerDTO) {
        Student student = Student.builder()
                .firstName(registerDTO.firstName())
                .lastName(registerDTO.lastName())
                .email(registerDTO.email())
                .password(passwordEncoder.encode(registerDTO.password()))
                .role(Role.ROLE_STUDENT)
                .build();

        student = studentRepository.save(student);
        return studentMapper.toDto(student);
    }
}
