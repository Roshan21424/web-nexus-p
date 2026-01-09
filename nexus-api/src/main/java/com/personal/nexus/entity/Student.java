package com.personal.nexus.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
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
