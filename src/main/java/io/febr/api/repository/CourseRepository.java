package io.febr.api.repository;

import io.febr.api.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByIdAndTeacherId(Long id, Long teacherId);

    List<Course> findAllByTeacherId(Long teacherId);

    @Query("SELECT c FROM Course c JOIN c.students s WHERE s.id = ?1")
    List<Course> findAllByStudentId(Long studentId);

    @Query("SELECT c FROM Course c JOIN c.students s WHERE c.id = ?1 AND s.id = ?2 ")
    Optional<Course> findByIdAndStudentId(Long id, Long studentId);
}
