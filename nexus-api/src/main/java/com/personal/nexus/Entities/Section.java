package com.personal.nexus.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private SectionEnum sectionEnum;

    @OneToOne(mappedBy = "classTeacherOf")
    private Teacher classTeacher;

    @OneToMany
    @JoinColumn(name = "teacher_id")
    private Set<Student> students;


    @OneToMany(mappedBy = "section")
    private Set<Subject> subjects;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String events;


    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String timetableImageBase64;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String semesterScheduleImageBase64;

    public enum SectionEnum {
        CSE_2025_A,
        CSE_2025_B,
        ECE_2025_A,
        MECH_2025_A;
    }
}
