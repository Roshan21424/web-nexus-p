package com.personal.nexus.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Teacher extends User {

    @OneToOne
    private Section classTeacherOf;

    @ManyToMany
    @JoinTable(
        name = "teacher_subject_sections",
        joinColumns = @JoinColumn(name = "teacher_id"),
        inverseJoinColumns = @JoinColumn(name = "section_id")
    )
    private Set<Section> subjectSections;

    @OneToMany(mappedBy = "teacher")
    private Set<Subject> subjects;

}
