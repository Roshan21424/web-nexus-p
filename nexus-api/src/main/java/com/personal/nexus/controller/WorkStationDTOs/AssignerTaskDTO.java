package com.personal.nexus.controller.WorkStationDTOs;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AssignerTaskDTO {
    private String by;
    private boolean access;
    private List<TaskContextDTO> context;
}
