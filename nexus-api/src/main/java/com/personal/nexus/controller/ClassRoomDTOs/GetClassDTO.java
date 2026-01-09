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
public class GetClassDTO {

    private String userRoleInSection;
    private String sectionName;
    private ClassTeacherDTO classTeacherDTO;
    private List<StudentDTO> studentDTOList;
    private List<SubjectDTO> subjectDTOList;
    private String events;
    private String timetableImageBase64;
}
