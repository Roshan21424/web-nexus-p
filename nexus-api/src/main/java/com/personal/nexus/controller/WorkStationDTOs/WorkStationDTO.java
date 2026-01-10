package com.personal.nexus.controller.WorkStationDTOs;


import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WorkStationDTO {

    private Long id;
    private String sectionName;
    private List<GroupedRemainderDTO> today_remainder;
    private List<GroupedTaskDTO> today_task;


}
