package com.personal.nexus.controller;


import com.personal.nexus.controller.WorkStationDTOs.DailyRemainderDTO;
import com.personal.nexus.controller.WorkStationDTOs.DailyTaskDTO;
import com.personal.nexus.controller.WorkStationDTOs.WorkStationDTO;
import com.personal.nexus.service.WorkStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/workstations")
public class WorkStationController {

  @Autowired
  private WorkStationService workStationService;

  @PostMapping("/create-workstation")
  private ResponseEntity<String> createWorkStation(@RequestParam Long sectionId){
    workStationService.createWorkStation(sectionId);
    return ResponseEntity.status(HttpStatus.OK).body("workstation has been created");
  }

  @GetMapping("/get-workstation/{workStationId}")
  private ResponseEntity<WorkStationDTO> getWorkstation(@PathVariable Long workStationId){
    return ResponseEntity.status(HttpStatus.OK).body(workStationService.getWorkStation(workStationId));
  }

  @PostMapping("/set-daily-remainder")
  private ResponseEntity<String> addDailyRemainder(@RequestBody DailyRemainderDTO dailyRemainderDTO, @RequestParam Long workStationId, @RequestParam Long teacherId, @RequestParam Set<Long> studentsId) {
    workStationService.addDailyRemainder(dailyRemainderDTO,workStationId,teacherId,studentsId);
    return ResponseEntity.status(HttpStatus.OK).body("daily remainder has been added");
  }

  @PostMapping("/set-daily-tasks")
  private ResponseEntity<String> addDailyTasks(@RequestBody DailyTaskDTO dailyTaskDTO, @RequestParam Long workStationId, @RequestParam Long teacherId, @RequestParam Set<Long> studentsId) {
    workStationService.addDailyTask(dailyTaskDTO,workStationId,teacherId,studentsId);
    return ResponseEntity.status(HttpStatus.OK).body("daily task has been added");
  }

}
