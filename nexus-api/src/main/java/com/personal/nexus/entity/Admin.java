package com.personal.nexus.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Admin extends User {
    @PrePersist
    protected void onCreate() {
        super.setRole(Role.ADMIN);
        super.setActive(true);
    }
}
