package com.personal.nexus.service;


import com.personal.nexus.DTO.SectionListDTO;
import com.personal.nexus.controller.ClassRoomDTOs.ClassTeacherDTO;
import com.personal.nexus.controller.ClassRoomDTOs.GetClassDTO;
import com.personal.nexus.controller.ClassRoomDTOs.StudentDTO;
import com.personal.nexus.controller.ClassRoomDTOs.SubjectDTO;
import com.personal.nexus.entity.*;
import com.personal.nexus.repository.SectionRepository;
import com.personal.nexus.repository.TeacherRepository;
import com.personal.nexus.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
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

    @Autowired
    private AuthService authService;


    public String getSectionName(Long sectionId){
        return  getSection(sectionId).getSectionEnum().name();
    }
    public List<Student> getStudentsOfSection(Long sectionId){
        Section section = getSection(sectionId);
        List<Student> students = new ArrayList<>(section.getStudents());
        return students;
    }
    public Teacher getClassTeacherOfSection(Long sectionId){
        return getSection(sectionId).getClassTeacher();
    }
    public List<Subject> getSubjectsOfSection(Long sectionId){
        return new ArrayList<>(getSection(sectionId).getSubjects());
    }
    public List<Teacher> getSubjectTeachersOfSection(Long sectionId){
        List<Teacher> subjectTeachers = new ArrayList<>();
        for(Subject subject: getSubjectsOfSection(sectionId)){
            subjectTeachers.add(subject.getTeacher());
        }
        return subjectTeachers;
    }
    public String getEventsOfSection(Long sectionId){
        return getSection(sectionId).getEvents();
    }
    public String getTimeTableOfSection(Long sectionId){
        return getSection(sectionId).getTimetableImageBase64();
    }
    public String getCurrentUserRoleInSection(Long sectionId) {

        User user = authService.getCurrentUser();

        if (user.getRole().name().equals("STUDENT")) {
            for (Student student : getStudentsOfSection(sectionId)) {
                if (user.getId().equals(student.getId())) {
                    return "STUDENT";
                }
            }
            return "NOT_RELATED";
        }

        if (user.getRole().name().equals("TEACHER")) {

            if (getClassTeacherOfSection(sectionId) != null &&
                    user.getId().equals(getClassTeacherOfSection(sectionId).getId())) {
                return "CLASS_TEACHER";
            }

            for (Teacher teacher : getSubjectTeachersOfSection(sectionId)) {
                if (user.getId().equals(teacher.getId())) {
                    return "SUBJECT_TEACHER";
                }
            }

            return "NOT_RELATED";
        }

        return "NOT_RELATED";
    }




    public GetClassDTO getClass(Long sectionId) {
        //get the current user section in the section
        String roleInSection =  getCurrentUserRoleInSection(sectionId);

        //create studentDTOs
        List<StudentDTO> studentDTOS = new ArrayList<>();
        for (Student student : getStudentsOfSection(sectionId)) {
            StudentDTO studentDTO = new StudentDTO(student.getId(), student.getName());
            studentDTOS.add(studentDTO);
        }

        //create subjectDTOs
        List<SubjectDTO> subjectDTOs = new ArrayList<>();
        for (Subject subject : getSubjectsOfSection(sectionId)) {
            SubjectDTO subjectDTO = new SubjectDTO(subject.getId(), subject.getName(), subject.getTeacher().getName());
            subjectDTOs.add(subjectDTO);
        }

        //create classTeacherDTOs
        Teacher classTeacher = getClassTeacherOfSection(sectionId);
        ClassTeacherDTO classTeacherDTO = new ClassTeacherDTO(classTeacher.getName());

        //returning the class DTO
        GetClassDTO getClassDTO = new GetClassDTO();
        getClassDTO.setSectionName(getSectionName(sectionId));//setting the section name
        getClassDTO.setClassTeacherDTO(classTeacherDTO);//setting the class teacher
        getClassDTO.setStudentDTOList(studentDTOS);//setting the students
        getClassDTO.setSubjectDTOList(subjectDTOs);//setting the subjects
        getClassDTO.setEvents(getEventsOfSection(sectionId));//setting the events
        getClassDTO.setTimetableImageBase64(getTimeTableOfSection(sectionId));//getting the timetable
        getClassDTO.setUserRoleInSection(roleInSection);

        return getClassDTO;
    }

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
