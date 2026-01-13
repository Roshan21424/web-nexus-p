package com.personal.nexus.repository;

import com.personal.nexus.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SectionRepository extends JpaRepository<Section, Long> {
    @Query("""
    SELECT s
    FROM Section s
    JOIN s.students st
    WHERE st.id = :studentId
""")
    Section findByStudentId(@Param("studentId") Long studentId);
}
