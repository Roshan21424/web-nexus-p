package com.personal.nexus.controller.WorkStationDTOs;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GroupedTaskDTO {
    private String to;
    private List<AssignerTaskDTO> assigner;
}
