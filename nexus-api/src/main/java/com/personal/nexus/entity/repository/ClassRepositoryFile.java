package com.personal.nexus.entity.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.personal.nexus.entity.Section;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClassRepositoryFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String size;
    private String uploadedBy;
    private String category;
    private String subject;
    private String url;
    private LocalDateTime uploadDate;

    @OneToOne
    @JoinColumn(name="section_id")
    @JsonIgnore
    private Section section;
}
