package com.personal.nexus.service;


import com.personal.nexus.DTO.StudentListDTO;
import com.personal.nexus.entity.Section;
import com.personal.nexus.entity.Student;
import com.personal.nexus.entity.User;
import com.personal.nexus.repository.SectionRepository;
import com.personal.nexus.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void assignStudentToSection(Long studentId,
                                       Long sectionId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));
       section.getStudents().add(student);
       sectionRepository.save(section);
    }

    public void createStudent(String name,
                              String email,
                              String password) {

        //create student
        Student student = new Student();
        student.setName(name);
        student.setEmail(email);
        student.setPassword(passwordEncoder.encode(password));
        student.setRole(User.Role.STUDENT);

        //save student
        studentRepository.save(student);
    }

    public List<StudentListDTO> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(student -> new StudentListDTO(
                        student.getId(),
                        student.getName(),
                        student.getEmail()
                ))
                .collect(Collectors.toList());
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
