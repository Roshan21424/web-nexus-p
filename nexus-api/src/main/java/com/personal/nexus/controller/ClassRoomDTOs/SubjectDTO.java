package com.personal.nexus.controller.ClassRoomDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDTO {
    private Long subjectID;
    private String subjectName;
    private String subjectTeacherName;
}
