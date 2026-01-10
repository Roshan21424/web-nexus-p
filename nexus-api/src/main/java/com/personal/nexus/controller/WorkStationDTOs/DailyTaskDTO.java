package com.personal.nexus.controller.WorkStationDTOs;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DailyTaskDTO {
    private Long id;
    private String description;
    private LocalDateTime startTime;
    private String status;
    private boolean forAll;
    private String fromTeacher;
    private List<String> toStudents;
}
