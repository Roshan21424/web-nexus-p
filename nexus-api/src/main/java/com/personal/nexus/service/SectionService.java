package com.personal.nexus.service;


import com.personal.nexus.DTO.SectionListDTO;
import com.personal.nexus.entity.Section;
import com.personal.nexus.entity.Student;
import com.personal.nexus.entity.Teacher;
import com.personal.nexus.repository.SectionRepository;
import com.personal.nexus.repository.StudentRepository;
import com.personal.nexus.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private WorkStationService workStationService;


    public Section getSection(Long sectionId){
        return sectionRepository.findById(sectionId).orElseThrow(()->new RuntimeException("Section not found"));
    }
    public void createSection(String sectionEnum) {

        //create  section
        Section section = new Section();
        section.setSectionEnum(Section.SectionEnum.valueOf(sectionEnum));

        //save  section
        sectionRepository.save(section);

        //create workstation and link with  section
        workStationService.createWorkStation(section.getId());
    }
    public void assignClassTeacherToSection(Long teacherId,
                                            Long sectionId) {

        //find teacher and section
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("teacher not found"));

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("section not found"));

        // check section already has a class teacher
        if (section.getClassTeacher() != null) {
            throw new RuntimeException("section already has a class teacher");
        }

        teacher.setClassTeacherOf(section);

        // Save owning side
        teacherRepository.save(teacher);
    }
    public List<SectionListDTO> getAllSections() {
        List<Section> sections = sectionRepository.findAll();
        return sections.stream()
                .map(section -> {
                    String classTeacherName = section.getClassTeacher() != null
                            ? section.getClassTeacher().getName()
                            : null;
                    Integer studentCount = section.getStudents() != null
                            ? section.getStudents().size()
                            : 0;

                    return new SectionListDTO(
                            section.getId(),
                            section.getSectionEnum().toString(),
                            classTeacherName,
                            studentCount
                    );
                })
                .collect(Collectors.toList());
    }
    public void deleteSection(Long id) {
        sectionRepository.deleteById(id);
    }



}
