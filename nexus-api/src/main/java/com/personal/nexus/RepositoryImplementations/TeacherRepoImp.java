package com.personal.nexus.RepositoryImplementations;

import com.personal.nexus.entity.Teacher;
import com.personal.nexus.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherRepoImp {

    @Autowired
    private TeacherRepository teacherRepository;

    public Teacher getTeacherFromDBById(Long teacherId){
       return teacherRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
    }
}
