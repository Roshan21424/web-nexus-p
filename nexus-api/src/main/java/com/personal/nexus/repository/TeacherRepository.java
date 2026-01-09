package com.personal.nexus.repository;


import com.personal.nexus.entity.Section;
import com.personal.nexus.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

}
