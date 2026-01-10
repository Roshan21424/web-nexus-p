package com.personal.nexus.repository;


import com.personal.nexus.entity.Section;
import com.personal.nexus.entity.Subject;
import com.personal.nexus.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findByTeacher(Teacher teacher);
    List<Subject> findBySection(Section section);
    Subject findByNameAndSection(String name, Section section);
}

