package com.personal.nexus.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherListDTO {
    private Long id;  // or teacherId
    private String name;
    private String email;
}