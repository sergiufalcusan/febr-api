package io.febr.api.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * A course is a class that a teacher teaches to a group of students.
 * A course has a name, a description, a schedule, a teacher, and a list of students.
 * A course can be taught by only one teacher.
 * A course can have many students.
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    // for this example schedule is a simple LocalDateTime representing start time
    // in a real application it could be a more complex object
    // incl. a list of dates, a list of days of the week, recurring dates, etc.
    @Column(nullable = false)
    private LocalDateTime schedule;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToMany(mappedBy = "courses")
    private Set<Student> students;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course that)) return false;

        return id != null && id.equals(that.id);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
