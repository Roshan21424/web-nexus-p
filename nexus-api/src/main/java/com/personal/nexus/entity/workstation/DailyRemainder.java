package com.personal.nexus.entity.workstation;


import com.personal.nexus.entity.Student;
import com.personal.nexus.entity.Teacher;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "daily_remainder")
public class DailyRemainder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @CreationTimestamp
    private LocalDateTime creationTime;

    private boolean forAll;

    @ManyToOne
    @JoinColumn(name="teacher_id",referencedColumnName = "id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name="workstation_id",referencedColumnName = "id")
    private WorkStation workStation;

    @ManyToMany
    @JoinTable(name = "remainder_student", joinColumns = @JoinColumn(name = "remainder_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Set<Student> students;

}
