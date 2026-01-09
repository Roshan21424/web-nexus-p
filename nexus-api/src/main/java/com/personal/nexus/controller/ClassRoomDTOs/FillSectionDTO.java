package com.personal.nexus.controller.ClassRoomDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FillSectionDTO {
    private Long sectionId;
    private Long classTeacherId;
    private List<Long> studentsIds;
    private List<Long> subjectIds;
}
