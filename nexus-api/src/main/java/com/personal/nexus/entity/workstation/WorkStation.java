package com.personal.nexus.entity.workstation;

import com.personal.nexus.entity.Section;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class WorkStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "section_id", referencedColumnName = "id")
    private Section section;

    @OneToMany(mappedBy = "workStation")
    private Set<DailyRemainder> dailyRemainders;

    @OneToMany(mappedBy = "workStation")
    private Set<DailyTask> dailyTasks;

}
