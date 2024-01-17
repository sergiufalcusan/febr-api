package io.febr.api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

/**
 * A student is a user who is enrolled in a course.
 * A student has a name, an email, and a list of courses.
 * A student can be enrolled in many courses.
 */
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Student extends User {
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "student_course",
            joinColumns = {@JoinColumn(name = "student_id")},
            inverseJoinColumns = {@JoinColumn(name = "course_id")}
    )
    private Set<Course> courses;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student that)) return false;

        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
