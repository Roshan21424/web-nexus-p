package com.personal.nexus.service;


import com.personal.nexus.DTO.SubjectListDTO;
import com.personal.nexus.entity.Section;
import com.personal.nexus.entity.Subject;
import com.personal.nexus.entity.Teacher;
import com.personal.nexus.repository.SectionRepository;
import com.personal.nexus.repository.SubjectRepository;
import com.personal.nexus.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private SectionRepository sectionRepository;



    public void createSubject(String name){

        //create subject
        Subject subject = new Subject();
        subject.setName(name);

        //save subject
        subjectRepository.save(subject);
    }
    public void assignTeacher(Long subjectId, Long teacherId) {

        //find subject and teacher
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        subject.setTeacher(teacher); //set teacher

        //save subject
        subjectRepository.save(subject);
    }
    public Subject assignSection(Long subjectId, Long sectionId) {
        //find subject and section
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        subject.setSection(section); //set section

        //save the subject
        return subjectRepository.save(subject);
    }
    public List<SubjectListDTO> getAllSubjects() {
        List<Subject> subjects = subjectRepository.findAll();
        return subjects.stream()
                .map(subject -> {
                    String teacherName = subject.getTeacher() != null
                            ? subject.getTeacher().getName()
                            : null;
                    return new SubjectListDTO(
                            subject.getId(),
                            subject.getName(),
                            teacherName

                    );
                })
                .collect(Collectors.toList());
    }
    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }

}
