package com.personal.nexus.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectListDTO {
    private Long id;  // or subjectId
    private String name;  // or subjectName
    private String teacherName;
//    private Integer sectionCount;
}