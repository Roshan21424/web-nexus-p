package com.personal.nexus.entity.workstation;

import com.personal.nexus.entity.Student;
import com.personal.nexus.entity.Teacher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="daily_task")
public class DailyTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @CreationTimestamp
    private LocalDateTime startTime;

    private String status;


    private boolean forAll;

    @ManyToOne
    @JoinColumn(name = "teacher_id",referencedColumnName = "id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name="workstation_id",referencedColumnName = "id")
    private WorkStation workStation;

    @ManyToMany
    @JoinTable(name = "task_student",joinColumns = @JoinColumn(name = "task_id"),inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Set<Student> students;

}
