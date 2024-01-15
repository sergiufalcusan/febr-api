package io.febr.api.service;

import io.febr.api.domain.Course;
import io.febr.api.domain.Teacher;
import io.febr.api.dto.CourseDTO;
import io.febr.api.factory.CourseFactory;
import io.febr.api.mapper.CourseMapper;
import io.febr.api.repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseTest {
    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private UserService userService;

    @Test
    public void testGetAllCourses() {
        when(courseRepository.findAll()).thenReturn(List.of(new Course(), new Course()));
        when(courseMapper.toDtoList(any())).thenReturn(List.of(CourseFactory.createResponseDTO(), CourseFactory.createResponseDTO()));

        List<CourseDTO.CreateResponse> courseDTOList = courseService.getAllCourses();
        assertThat(courseDTOList.size()).isEqualTo(2);
    }

    @Test
    public void testGetAllCoursesEmptyList() {
        when(courseRepository.findAll()).thenReturn(List.of());
        when(courseMapper.toDtoList(any())).thenReturn(List.of());

        List<CourseDTO.CreateResponse> courseDTOList = courseService.getAllCourses();
        assertThat(courseDTOList.size()).isEqualTo(0);
    }

    @Test
    public void testGetCourseById() {
        when(courseRepository.findById(any())).thenReturn(Optional.of(new Course()));
        when(courseMapper.toDto(any())).thenReturn(CourseFactory.createResponseDTO());

        CourseDTO.CreateResponse courseDTO = courseService.getCourseById(1L);
        assertThat(courseDTO).isNotNull();
    }

    @Test
    public void testGetCourseByIdNotFound() {
        when(courseRepository.findById(any())).thenReturn(Optional.empty());
        Exception exception = assertThrows(NoSuchElementException.class, () -> courseService.getCourseById(1L));
        assertThat(exception.getMessage()).isEqualTo("No value present");
    }

    @Test
    public void testCreateCourse() {
        when(courseRepository.save(any())).thenReturn(new Course());
        when(courseMapper.toDto(any())).thenReturn(CourseFactory.createResponseDTO());
        when(courseMapper.createDtoToEntity(any())).thenReturn(new Course());
        when(userService.getCurrentTeacherUser()).thenReturn(new Teacher());
        CourseDTO.CreateResponse courseDTO = courseService.createCourse(CourseFactory.createRequestDTO());
        assertThat(courseDTO).isNotNull();
    }

    @Test
    public void testUpdateCourse() {
        when(courseRepository.save(any())).thenReturn(new Course());
        when(courseMapper.toDto(any())).thenReturn(CourseFactory.createResponseDTO());
        when(courseRepository.findById(any())).thenReturn(Optional.of(new Course()));
        when(userService.getTeacherById(any())).thenReturn(new Teacher());

        CourseDTO.CreateResponse courseDTO = courseService.updateCourse(1L, CourseFactory.updateRequestDTO());
        assertThat(courseDTO).isNotNull();
    }

    @Test
    public void testDeleteCourse() {
        courseService.deleteCourse(1L);
    }

    @Test
    public void testDeleteAllCourses() {
        courseService.deleteAllCourses();
    }
}
