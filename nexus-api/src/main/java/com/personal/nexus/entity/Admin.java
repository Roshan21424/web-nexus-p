package com.personal.nexus.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Admin extends User {
    @PrePersist
    protected void onCreate() {
        super.setRole(Role.ADMIN);
        super.setActive(true);
    }
}
