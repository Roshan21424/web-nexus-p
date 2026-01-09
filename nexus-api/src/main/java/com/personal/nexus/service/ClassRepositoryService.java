package com.personal.nexus.service;

import com.personal.nexus.RepositoryImplementations.ClassRepositoryFileRepoImp;
import com.personal.nexus.entity.repository.ClassRepositoryFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassRepositoryService {

    @Autowired
    private ClassRepositoryFileRepoImp classRepositoryFileRepoImp;
    public List<ClassRepositoryFile> getClassRepository(Long sectionId){
      return classRepositoryFileRepoImp.getAllClassRepositoryFilesBySectionId(sectionId);
    }
}
