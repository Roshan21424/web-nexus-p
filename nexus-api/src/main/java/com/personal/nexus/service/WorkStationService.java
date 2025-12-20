package com.personal.nexus.service;

import com.personal.nexus.entity.workstation.WorkStation;
import com.personal.nexus.repository.WorkStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


@Service
public class WorkStationService {

    @Autowired
    private WorkStationRepository workStationRepository;
    @Autowired
    @Lazy
    private SectionService sectionService;

    public void createWorkStation(Long sectionId){

      //create workstation
      WorkStation workStation = new WorkStation();
      workStation.setSection(sectionService.getSection(sectionId));

      //save workstation
      workStationRepository.save(workStation);
    }
}