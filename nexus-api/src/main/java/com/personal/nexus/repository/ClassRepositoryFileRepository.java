package com.personal.nexus.repository;

import com.personal.nexus.entity.repository.ClassRepositoryFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepositoryFileRepository extends JpaRepository<ClassRepositoryFile,Long> {
    List<ClassRepositoryFile> findBySection_Id(Long sectionId);

}
