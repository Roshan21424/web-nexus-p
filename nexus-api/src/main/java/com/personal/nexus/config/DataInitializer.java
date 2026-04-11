package com.personal.nexus.config;

import com.personal.nexus.entity.*;
import com.personal.nexus.entity.workstation.WorkStation;
import com.personal.nexus.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired MongoTemplate mongoTemplate;
    @Autowired AdminRepository adminRepository;
    @Autowired UserRepository userRepository;
    @Autowired TeacherRepository teacherRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired SectionRepository sectionRepository;
    @Autowired SubjectRepository subjectRepository;
    @Autowired WorkStationRepository workStationRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @PostConstruct
    public void checkMongo() {
        System.out.println("Mongo DB name = " + mongoTemplate.getDb().getName());
    }

    @Override
    public void run(ApplicationArguments args) {

        // Only seed if DB is empty
        if (userRepository.count() > 0) {
            System.out.println("✅ Data already seeded. Skipping.");
            return;
        }

        System.out.println("🚀 Seeding college data...");

        // ─── ADMIN ───────────────────────────────────────────────────────────────
        Admin admin = new Admin();
        admin.setName("admin");
        admin.setEmail("admin@college.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(User.Role.ADMIN);
        admin.setActive(true);
        admin.setEnabled(true);
        admin.setAccountNonExpired(true);
        admin.setAccountNonLocked(true);
        admin.setCredentialsNonExpired(true);
        adminRepository.save(admin);
        System.out.println("👤 Admin created  →  admin / admin123");

        // ─── TEACHERS ────────────────────────────────────────────────────────────
        String[][] teacherData = {
            // { username, email, plainPassword, subject1, subject2 }
            {"prof_sharma",    "sharma@college.com",    "teacher123", "Data Structures",       "Algorithms"},
            {"prof_verma",     "verma@college.com",     "teacher123", "Operating Systems",     "Computer Networks"},
            {"prof_mehta",     "mehta@college.com",     "teacher123", "Database Management",   "SQL Lab"},
            {"prof_gupta",     "gupta@college.com",     "teacher123", "Mathematics",           "Discrete Math"},
            {"prof_reddy",     "reddy@college.com",     "teacher123", "Electronics",           "Circuit Design"},
            {"prof_nair",      "nair@college.com",      "teacher123", "Physics",               "Engineering Physics"},
            {"prof_joshi",     "joshi@college.com",     "teacher123", "Software Engineering",  "Project Management"},
            {"prof_pillai",    "pillai@college.com",    "teacher123", "Machine Learning",      "AI Fundamentals"},
            {"prof_bose",      "bose@college.com",      "teacher123", "Compiler Design",       "Automata Theory"},
            {"prof_kapoor",    "kapoor@college.com",    "teacher123", "Web Technologies",      "Cloud Computing"},
            {"prof_saxena",    "saxena@college.com",    "teacher123", "Computer Graphics",     "Image Processing"},
            {"prof_rao",       "rao@college.com",       "teacher123", "Embedded Systems",      "IoT"},
            {"prof_iyer",      "iyer@college.com",      "teacher123", "Engineering Maths",     "Probability"},
            {"prof_kulkarni",  "kulkarni@college.com",  "teacher123", "Microprocessors",       "VLSI Design"},
            {"prof_pandey",    "pandey@college.com",    "teacher123", "English Communication", "Technical Writing"},
        };

        List<Teacher> teachers = new ArrayList<>();
        for (String[] td : teacherData) {
            Teacher t = new Teacher();
            t.setName(td[0]);
            t.setEmail(td[1]);
            t.setPassword(passwordEncoder.encode(td[2]));
            t.setRole(User.Role.TEACHER);
            t.setEnabled(true);
            t.setAccountNonExpired(true);
            t.setAccountNonLocked(true);
            t.setCredentialsNonExpired(true);
            teachers.add(teacherRepository.save(t));
            System.out.printf("👨‍🏫 Teacher  →  %-20s / %s%n", td[0], td[2]);
        }

        // ─── SECTIONS ────────────────────────────────────────────────────────────
        Section.SectionEnum[] sectionEnums = Section.SectionEnum.values(); // CSE_2025_A, CSE_2025_B, ECE_2025_A, MECH_2025_A

        // We'll use the 4 available enums and cycle the teacher assignments.
        // If you want more sections just add more SectionEnum values in Section.java
        List<Section> sections = new ArrayList<>();
        for (int i = 0; i < sectionEnums.length; i++) {
            Section section = new Section();
            section.setSectionEnum(sectionEnums[i]);
            section.setStudents(new HashSet<>());
            section.setEvents("📅 Mid-semester exams: May 15-20 | 🎉 Annual fest: June 5 | 📝 Project submission: June 25");
            sections.add(sectionRepository.save(section));
            System.out.println("🏫 Section created: " + sectionEnums[i].name());
        }

        // Assign class teachers (one per section)
        for (int i = 0; i < sections.size(); i++) {
            Teacher classTeacher = teachers.get(i % teachers.size());
            classTeacher.setClassTeacherOf(sections.get(i));
            teacherRepository.save(classTeacher);
        }

        // ─── SUBJECTS ────────────────────────────────────────────────────────────
        // Each section gets 6 subjects assigned from the teacher pool
        String[][] subjectPool = {
            {"Data Structures",       "0"},  // teacher index
            {"Algorithms",            "0"},
            {"Database Management",   "2"},
            {"Operating Systems",     "1"},
            {"Mathematics",           "3"},
            {"Software Engineering",  "6"},
            {"Electronics",           "4"},
            {"Computer Networks",     "1"},
            {"Machine Learning",      "7"},
            {"Web Technologies",      "9"},
            {"Compiler Design",       "8"},
            {"English Communication", "14"},
        };

        for (int s = 0; s < sections.size(); s++) {
            Section section = sections.get(s);
            // Each section gets subjects at offset so they vary a bit
            int offset = s * 3;
            for (int j = 0; j < 6; j++) {
                int idx = (offset + j) % subjectPool.length;
                Subject subject = new Subject();
                subject.setName(subjectPool[idx][0]);
                subject.setSection(section);
                subject.setTeacher(teachers.get(Integer.parseInt(subjectPool[idx][1])));
                subjectRepository.save(subject);
            }
        }

        // ─── STUDENTS ────────────────────────────────────────────────────────────
        // 30 students per section
        String[] firstNames = {
            "Aarav","Aditya","Akash","Ananya","Anjali","Arjun","Aryan","Ayaan","Ayush","Bhavya",
            "Chirag","Deepak","Divya","Esha","Gaurav","Harsha","Ishaan","Isha","Jatin","Kavya",
            "Kiran","Kritika","Lakshmi","Manish","Meera","Mohit","Naman","Neha","Nikhil","Nitin",
            "Palak","Parth","Pooja","Prachi","Pranav","Priya","Rahul","Raj","Ritu","Rohan",
            "Sakshi","Sanya","Shubham","Sneha","Sumit","Tanvi","Tushar","Uday","Varun","Vishal"
        };

        String[] lastNames = {
            "Sharma","Verma","Gupta","Singh","Patel","Kumar","Joshi","Nair","Reddy","Mehta",
            "Kapoor","Bose","Rao","Iyer","Pillai","Saxena","Pandey","Mishra","Tiwari","Agarwal"
        };

        int studentCounter = 1;
        for (int s = 0; s < sections.size(); s++) {
            Section section = sections.get(s);
            String sectionCode = section.getSectionEnum().name().toLowerCase().replace("_", "");

            for (int st = 0; st < 32; st++) {
                String firstName = firstNames[(studentCounter - 1) % firstNames.length];
                String lastName  = lastNames[(studentCounter - 1) % lastNames.length];
                String username  = firstName.toLowerCase() + "_" + lastName.toLowerCase() + "_" + studentCounter;
                String email     = username + "@student.college.com";
                String password  = "student123";  // same password for all students

                Student student = new Student();
                student.setName(username);
                student.setEmail(email);
                student.setPassword(passwordEncoder.encode(password));
                student.setRole(User.Role.STUDENT);
                student.setStudentRole(Student.StudentRole.NORMAL);
                student.setEnabled(true);
                student.setAccountNonExpired(true);
                student.setAccountNonLocked(true);
                student.setCredentialsNonExpired(true);

                // First student of section becomes CR
                if (st == 0) student.setStudentRole(Student.StudentRole.CR);

                Student saved = studentRepository.save(student);
                section.getStudents().add(saved);
                studentCounter++;
            }

            sectionRepository.save(section);
            System.out.printf("👨‍🎓 Section %-15s → 32 students added%n", section.getSectionEnum().name());
        }

        // ─── WORKSTATIONS ────────────────────────────────────────────────────────
        for (Section section : sections) {
            WorkStation ws = new WorkStation();
            ws.setSection(section);
            workStationRepository.save(ws);
        }

        // ─── SUMMARY ─────────────────────────────────────────────────────────────
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    🎓 SEEDING COMPLETE                       ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.printf("║  👤 Admin     :  admin              /  admin123%n");
        System.out.println("║──────────────────────────────────────────────────────────────║");
        System.out.println("║  👨‍🏫 Teachers  :  prof_sharma .. prof_pandey  /  teacher123   ║");
        System.out.println("║  (15 teachers, use exact usernames from logs above)          ║");
        System.out.println("║──────────────────────────────────────────────────────────────║");
        System.out.printf("║  👨‍🎓 Students  :  %d students (32 per section) / student123%n", (studentCounter - 1));
        System.out.println("║  Format: firstname_lastname_N  (e.g. aarav_sharma_1)         ║");
        System.out.println("║──────────────────────────────────────────────────────────────║");
        System.out.println("║  🏫 Sections  :  CSE_2025_A, CSE_2025_B, ECE_2025_A,        ║");
        System.out.println("║                  MECH_2025_A                                  ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝\n");
    }
}