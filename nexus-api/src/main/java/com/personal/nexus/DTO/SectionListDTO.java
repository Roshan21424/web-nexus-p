package com.personal.nexus.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectionListDTO {
    private Long id;  // or sectionId
    private String sectionName;  // or name
    private String classTeacherName;
    private Integer studentCount;
}
