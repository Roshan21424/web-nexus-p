package com.personal.nexus.controller;

import com.personal.nexus.DTO.*;
import com.personal.nexus.service.SectionService;
import com.personal.nexus.service.StudentService;
import com.personal.nexus.service.SubjectService;
import com.personal.nexus.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private SectionService sectionService;


    /* create endpoints */
    @PostMapping("/add-section")
    public ResponseEntity<String> createSection(@RequestBody CreateSectionDTO createSectionDTO){
        sectionService.createSection(createSectionDTO.getSectionEnum());
        return ResponseEntity.status(HttpStatus.OK).body("section created");
    }

    @PostMapping("/add-teacher")
    public ResponseEntity<String> createTeacher(@RequestBody CreateTeacherDTO createTeacherDTO) {
         teacherService.createTeacher(
                 createTeacherDTO.getName(),
                 createTeacherDTO.getEmail(),
                 createTeacherDTO.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body("teacher created");
    }

    @PostMapping("/add-student")
    public ResponseEntity<String> createStudent(@RequestBody CreateStudentDTO createStudentDTO) {
         studentService.createStudent(
                 createStudentDTO.getName(),
                 createStudentDTO.getEmail(),
                 createStudentDTO.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body("student created");
    }

    @PostMapping("/add-subject")
    public ResponseEntity<String> createSubject(@RequestBody CreateSubjectDTO createSubjectDTO){
        subjectService.createSubject(createSubjectDTO.getName());
        return ResponseEntity.status(HttpStatus.OK).body("subject created");
    }


    /* assign endpoints */
    @PutMapping("/{subjectId}/assign-teacher/{teacherId}")
    public ResponseEntity<String> assignTeacherToSubject(
            @PathVariable Long subjectId,
            @PathVariable Long teacherId) {

        subjectService.assignTeacher(subjectId, teacherId);
        return ResponseEntity.status(HttpStatus.OK).body("assign teacher to subject");
    }

    @PutMapping("/{subjectId}/assign-section/{sectionId}")
    public ResponseEntity<String> assignSectionToSubject(
            @PathVariable Long subjectId,
            @PathVariable Long sectionId){

        subjectService.assignSection(subjectId, sectionId);
        return ResponseEntity.status(HttpStatus.OK).body("assign section to subject");
    }

    @PutMapping("/{teacherId}/assign-class-teacher/{sectionId}")
    public ResponseEntity<String> assignClassTeacherToSection(
            @PathVariable Long teacherId,
            @PathVariable Long sectionId){

        sectionService.assignClassTeacherToSection(teacherId,sectionId);
        return ResponseEntity.status(HttpStatus.OK).body("assign class teacher to section");

    }

    @PutMapping("/{studentId}/assign-student/{sectionId}")
    public ResponseEntity<String> assignStudentToSection(@PathVariable Long studentId,@PathVariable Long sectionId){
        studentService.assignStudentToSection(studentId,sectionId);
        return ResponseEntity.status(HttpStatus.OK).body("assign student to section");
    }


    /* fetch endpoints */
    @GetMapping("/teachers")
    public ResponseEntity<List<TeacherListDTO>> getAllTeachers() {
        List<TeacherListDTO> teachers = teacherService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentListDTO>> getAllStudents() {
        List<StudentListDTO> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/sections")
    public ResponseEntity<List<SectionListDTO>> getAllSections() {
        List<SectionListDTO> sections = sectionService.getAllSections();
        return ResponseEntity.ok(sections);
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<SubjectListDTO>> getAllSubjects() {
        List<SubjectListDTO> subjects = subjectService.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }


    /* delete endpoints */
    @DeleteMapping("/teachers/{id}")
    public ResponseEntity<String> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.ok("Teacher deleted");
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok("Student deleted");
    }

    @DeleteMapping("/sections/{id}")
    public ResponseEntity<String> deleteSection(@PathVariable Long id) {
        sectionService.deleteSection(id);
        return ResponseEntity.ok("Section deleted");
    }

    @DeleteMapping("/subjects/{id}")
    public ResponseEntity<String> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.ok("Subject deleted");
    }
}
