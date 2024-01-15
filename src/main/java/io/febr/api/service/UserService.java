package io.febr.api.service;

import io.febr.api.domain.Role;
import io.febr.api.domain.Teacher;
import io.febr.api.dto.TeacherDTO;
import io.febr.api.mapper.TeacherMapper;
import io.febr.api.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class UserService {
    private TeacherRepository teacherRepository;
    private PasswordEncoder passwordEncoder;
    private TeacherMapper teacherMapper;

    public Teacher getCurrentTeacherUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return teacherRepository.findByEmail(userDetails.getUsername()).orElseThrow();
    }

    public Teacher getTeacherById(Long teacherId) {
        return teacherRepository.findById(teacherId).orElseThrow();
    }

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
}
