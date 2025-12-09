package com.personal.nexus.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Student extends User {


    @Enumerated(EnumType.STRING)
    private StudentRole studentRole;

    public enum StudentRole {
        GR,
        CR,
        NORMAL
    }
}
