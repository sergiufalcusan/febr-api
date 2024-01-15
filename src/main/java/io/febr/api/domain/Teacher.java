package io.febr.api.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Teacher extends User {
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Course> course;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher that)) return false;

        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}

