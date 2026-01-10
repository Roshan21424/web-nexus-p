package com.personal.nexus.service;

import com.personal.nexus.RepositoryImplementations.TeacherRepoImp;
import com.personal.nexus.controller.WorkStationDTOs.*;
import com.personal.nexus.entity.Student;
import com.personal.nexus.entity.Teacher;
import com.personal.nexus.entity.workstation.DailyRemainder;
import com.personal.nexus.entity.workstation.DailyTask;
import com.personal.nexus.entity.workstation.WorkStation;
import com.personal.nexus.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class WorkStationService {

    @Autowired
    private WorkStationRepository workStationRepository;
    @Autowired
    @Lazy
    private SectionService sectionService;
    @Autowired
    private DailyRemainderService dailyRemainderService;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private DailyRemainderRepository dailyRemainderRepository;
    @Autowired
    private DailyTaskRepository dailyTaskRepository;
    @Autowired
    private TeacherRepoImp teacherRepoImp;
    @Autowired
    private DailyTaskService dailyTaskService;

    public WorkStation getWorkStationBySectionId(Long sectionId){
        return workStationRepository.findBySection_Id(sectionId).orElseThrow(()->new RuntimeException("workstation not found"));
    }
    public WorkStation getWorkStationFromDB(Long workstationId){
        return workStationRepository.findById(workstationId).orElseThrow(() -> new RuntimeException("WorkStation not found"));
    }
    public void createWorkStation(Long sectionId){

      //create workstation
      WorkStation workStation = new WorkStation();
      workStation.setSection(sectionService.getSection(sectionId));

      //save workstation
      workStationRepository.save(workStation);
    }
    public DailyRemainder addDailyRemainder(DailyRemainderDTO dailyRemainderDTO,Long workstationId,Long teacherId, Set<Long> studentIds){


        //get the workstation
        WorkStation workstation = workStationRepository.findById(workstationId).orElseThrow(() -> new RuntimeException("WorkStation not found"));

        //get the teacher
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));


        DailyRemainder dailyRemainder = new DailyRemainder();

        dailyRemainder.setWorkStation(workstation); //set the workstation
        dailyRemainder.setTeacher(teacher); //set the teacher
        dailyRemainder.setDescription(dailyRemainderDTO.getDescription());
        dailyRemainder.setForAll(dailyRemainderDTO.isForAll());

        if (!dailyRemainderDTO.isForAll() && studentIds != null && !studentIds.isEmpty()) {

            Set<Student> students = studentRepository.findAllById(studentIds).stream().collect(java.util.stream.Collectors.toSet());
            dailyRemainder.setStudents(students); //set the students
        }

        return dailyRemainderRepository.save(dailyRemainder);

    }
    private List<GroupedRemainderDTO> groupRemainders(List<DailyRemainder> remainders) {
        Map<String, Map<String, List<String>>> grouped = new LinkedHashMap<>();

        for (DailyRemainder dr : remainders) {
            String teacher = dr.getTeacher() != null ? dr.getTeacher().getName() : "Unknown";
            List<String> recipients = dr.isForAll()
                    ? List.of("ALL")
                    : dr.getStudents().stream().map(Student::getName).toList();

            for (String to : recipients) {
                grouped.putIfAbsent(to, new LinkedHashMap<>());
                grouped.get(to).putIfAbsent(teacher, new ArrayList<>());
                grouped.get(to).get(teacher).add(dr.getDescription());
            }
        }

        return grouped.entrySet().stream()
                .map(entry -> new GroupedRemainderDTO(
                        entry.getKey(),
                        entry.getValue().entrySet().stream()
                                .map(inner -> new AssignerRemainderDTO(inner.getKey(),
                                        inner.getKey().equalsIgnoreCase("you"),
                                        inner.getValue()))
                                .toList()
                ))
                .toList();
    }
    public DailyTask addDailyTask(DailyTaskDTO dailyTaskDTO, Long workstationId, Long teacherId, Set<Long> studentIds){


        //get the workstation
        WorkStation workstation = getWorkStationFromDB(workstationId);

        //get the teacher
        Teacher teacher = teacherRepoImp.getTeacherFromDBById(teacherId);

        DailyTask dailyTask= new DailyTask();
        dailyTask.setWorkStation(workstation); //set the workstation
        dailyTask.setTeacher(teacher); //set the teacher
        dailyTask.setDescription(dailyTask.getDescription());
        dailyTask.setForAll(dailyTaskDTO.isForAll());
        if (!dailyTaskDTO.isForAll() && studentIds != null && !studentIds.isEmpty()) {

            Set<Student> students = studentRepository.findAllById(studentIds).stream().collect(java.util.stream.Collectors.toSet());
            dailyTask.setStudents(students); //set the students
        }
        dailyTask.setStatus("Pending");

        return dailyTaskRepository.save(dailyTask);
    }
    private List<GroupedTaskDTO> groupTasks(List<DailyTask> tasks) {
        Map<String, Map<String, List<TaskContextDTO>>> grouped = new LinkedHashMap<>();

        for (DailyTask dt : tasks) {
            String teacher = dt.getTeacher() != null ? dt.getTeacher().getName() : "Unknown";
            List<String> recipients = dt.isForAll()
                    ? List.of("ALL")
                    : dt.getStudents().stream().map(Student::getName).toList();

            for (String to : recipients) {
                grouped.putIfAbsent(to, new LinkedHashMap<>());
                grouped.get(to).putIfAbsent(teacher, new ArrayList<>());
                grouped.get(to).get(teacher).add(new TaskContextDTO(dt.getDescription(), dt.getStatus()));
            }
        }

        return grouped.entrySet().stream()
                .map(entry -> new GroupedTaskDTO(
                        entry.getKey(),
                        entry.getValue().entrySet().stream()
                                .map(inner -> new AssignerTaskDTO(inner.getKey(),
                                        inner.getKey().equalsIgnoreCase("you"),
                                        inner.getValue()))
                                .toList()
                ))
                .toList();
    }
    public WorkStationDTO getWorkStation(Long sectionId) {

        //get the workstation
        WorkStation workStation = getWorkStationBySectionId(sectionId);

        //get the role of user in section
        String roleOfUserInSection = sectionService.getCurrentUserRoleInSection(sectionId);

        //get the dailyRemainders and dailyTasks
        List<DailyRemainder> dailyRemainders = dailyRemainderService.getDailyRemaindersByWorkStationId(workStation.getId());
        List<DailyTask> dailyTasks = dailyTaskService.getDailyTaskRepositoryByWorkStationId(workStation.getId());

        // Group dailyRemainders and dailyTasks
        List<GroupedRemainderDTO> groupedDailyRemainders = groupRemainders(dailyRemainders);
        List<GroupedTaskDTO> groupedDailyTasks = groupTasks(dailyTasks);

        //return the dto
        WorkStationDTO dto = new WorkStationDTO();
        dto.setId(workStation.getId());
        dto.setSectionName(sectionService.getSectionName(sectionId));
        dto.setToday_remainder(groupedDailyRemainders);
        dto.setToday_task(groupedDailyTasks);

        return dto;
    }

}