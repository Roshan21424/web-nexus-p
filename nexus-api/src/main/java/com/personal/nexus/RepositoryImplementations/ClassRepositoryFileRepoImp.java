package com.personal.nexus.RepositoryImplementations;

import com.personal.nexus.entity.repository.ClassRepositoryFile;
import com.personal.nexus.repository.ClassRepositoryFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassRepositoryFileRepoImp {

    @Autowired
    private ClassRepositoryFileRepository classRepositoryFileRepository;

    public List<ClassRepositoryFile> getAllClassRepositoryFilesBySectionId(Long sectionId){
        return classRepositoryFileRepository.findBySection_Id(sectionId);
    }
}
