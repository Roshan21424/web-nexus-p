package com.personal.nexus.service;



import com.personal.nexus.DTO.TeacherListDTO;
import com.personal.nexus.entity.Teacher;
import com.personal.nexus.entity.User;
import com.personal.nexus.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Teacher createTeacher(String name,
                                 String email,
                                 String password ) {
        //create a teacher
        Teacher teacher = new Teacher();
        teacher.setName(name);
        teacher.setEmail(email);
        teacher.setPassword(passwordEncoder.encode(password));
        teacher.setRole(User.Role.TEACHER);

        //save the teacher
        return teacherRepository.save(teacher);
    }

    public List<TeacherListDTO> getAllTeachers() {
        List<Teacher> teachers = teacherRepository.findAll();
        return teachers.stream()
                .map(teacher -> new TeacherListDTO(
                        teacher.getId(),
                        teacher.getName(),
                        teacher.getEmail()
                ))
                .collect(Collectors.toList());
    }

    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }

}
